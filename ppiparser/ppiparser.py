#!/usr/bin/python

import string
import sys


XML_TEMPLATE = '''<?xml version="1.0" encoding="UTF-8"?>
<QuestionGroup xmlns="http://www.mifos.org/QuestionGroupDefinition"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://www.mifos.org/QuestionGroupDefinition QuestionGroupDefinition.xsd ">
    <ppi>true</ppi>
    <title>{TITLE}</title>
    <editable>false</editable>
    <eventSource>
        <event>Create</event>
        <source>Client</source>
    </eventSource>
    <section order="1">
        <name>Survey Administration</name>
        <question order="1">
            <title>Date Survey Was Taken</title>
            <type>DATE</type>
        </question>
    </section>
    <section order="2">
        <name>PPI Questions</name>
{QUESTIONS}
    </section>
</QuestionGroup>
'''

CHOICE_TEMPLATE = \
'''            <choice order="{ORDER}">
                <value>{VALUE}</value>
            </choice>
'''

SINGLE_QUESTION_TEMPLATE = \
'''        <question order="{ORDER}">
            <title>{TITLE}</title>
            <type>SINGLE_SELECT</type>
{CHOICES}
            <mandatory>true</mandatory>
        </question>
'''

#####################################################################################

SQL_CASE_TEMPLATE = '''case
{WHENS}
end'''

SQL_CASE_WHEN_TEMPLATE = '''when answers.Q{NUMBER}='{ANSWER}' then {VALUE}'''

GROUP_CONCAT_TEMPLATE = "GROUP_CONCAT(if(q.short_name = '{QUESTION}', qgr.response, NULL)) AS 'Q{NUMBER}'"

SQL_TEMPLATE = '''select answers.survey_id, 1 as points_version, date(str_to_date(answers.date_survey_taken, '%d/%m/%Y')) as date_survey_taken, answers.entity_id, answers.entity_type_id,

(
{CASES}
) 
as ppi_score
from
(SELECT
qg.id as survey_id,
GROUP_CONCAT(if(q.short_name = 'Date Survey Was Taken', qgr.response, NULL)) AS 'date_survey_taken',
qgi.entity_id as entity_id,
es.entity_type_id as entity_type_id,
{CONCATS}
FROM question_group_response qgr, question_group_instance qgi, question_group qg, sections_questions sq, questions q, event_sources es
WHERE qgr.question_group_instance_id = qgi.id and qgr.sections_questions_id = sq.id and sq.question_id = q.question_id and qgi.question_group_id = qg.id and qg.title="{TITLE}" and qgi.event_source_id = es.id
GROUP BY question_group_instance_id) as answers
'''

def sql(qs, title='Unknown'):
    cases = []
    for (qnum, q) in enumerate(qs):
        whens = [SQL_CASE_WHEN_TEMPLATE.format(NUMBER=qnum+1, ANSWER=x[0], VALUE=x[1]) for x in q[1]]
        case = SQL_CASE_TEMPLATE.format(WHENS='\n'.join(whens))
        cases.append(case)
    group_concats = [GROUP_CONCAT_TEMPLATE.format(QUESTION=q[0], NUMBER=qnum+1) for (qnum, q) in enumerate(qs)]
    return SQL_TEMPLATE.format(CASES=' +\n'.join(cases), CONCATS=',\n'.join(group_concats), TITLE=title)


def xml(qs, title='Unknown'):
    questions = []
    for (qnum, q) in enumerate(qs):
        choices = [CHOICE_TEMPLATE.format(ORDER=i+1, VALUE=val) for (i, val) in enumerate(map(lambda x:x[0], q[1]))]
        question = SINGLE_QUESTION_TEMPLATE.format(ORDER=qnum+1, TITLE=q[0], CHOICES=''.join(choices))
        questions.append(question)
    return XML_TEMPLATE.format(TITLE=title, QUESTIONS=''.join(questions))

def parse_questions(f):
    #import ipdb; ipdb.set_trace()
    no = 1
    letter = 'Z'
    question = None
    answers = None
    for line in open(f):
        if not line.strip():
            continue
        if line.strip().startswith('%d.' % no):
            if no > 1:
                yield (question, answers)
            no += 1
            question = line[line.find('.')+1:].strip()
            letter_it = iter(string.ascii_uppercase)
            letter = letter_it.next()
            answers = []
        if line.strip().startswith('%s.' % letter):
            answers.append(line[line.find('.')+1:].strip())
            letter = letter_it.next()
        if '.' not in line.strip() and line.strip().isdigit():
            answers[-1] = (answers[-1], int(line.strip()))
        if 'Total score:' in line:
            yield (question, answers)
            return

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'Txt file needed as argument'
        sys.exit(1)
    filename = sys.argv[1]
    country_name = filename.split()[0]
    if '.' in country_name:
        country_name = country_name.split('.')[0]
    title = 'PPI Survey %s' % country_name.capitalize()
    qs = list(parse_questions(filename))
    sql_out = sql(qs, title)
    xml_out = xml(qs, title)
    with open(country_name.capitalize() + 'PPIScore.sql', 'w') as sql_f:
        sql_f.write(sql_out)
    with open('PPISurvey' + country_name.upper() + '.xml', 'w') as xml_f:
        xml_f.write(xml_out)
