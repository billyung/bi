select answers.survey_id, 0 as points_version, date(str_to_date(answers.date_survey_taken, '%d/%m/%Y')) as date_survey_taken, answers.entity_id, answers.entity_type_id,

(
case
when answers.Q1='Four or more' then 0
when answers.Q1='Three' then 3
when answers.Q1='Two' then 8
when answers.Q1='One' then 11
when answers.Q1='None' then 19
end +
case
when answers.Q2='No' then 0
when answers.Q2='Yes' then 3
when answers.Q2='No children ages 6–14' then 3
end +
case
when answers.Q3='Not Port-a-Prince' then 0
when answers.Q3='Port-a-Prince' then 15
end +
case
when answers.Q4='No' then 0
when answers.Q4='Yes' then 7
end +
case
when answers.Q5='Earth' then 0
when answers.Q5='Concrete or other' then 4
when answers.Q5='Ceramic or wood planks' then 12
end +
case
when answers.Q6='No' then 0
when answers.Q6='Yes' then 7
end +
case
when answers.Q7='No' then 0
when answers.Q7='Yes' then 12
end +
case
when answers.Q8='None' then 2
when answers.Q8='One' then 0
when answers.Q8='Two or three' then 5
when answers.Q8='Four or more' then 11
end +
case
when answers.Q9='Straw, palm leaves, or other' then 0
when answers.Q9='Iron' then 4
when answers.Q9='Concrete' then 9
end +
case
when answers.Q10='No' then 0
when answers.Q10='Yes' then 5
end
) 
as ppi_score
from
(SELECT
qg.id as survey_id,
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_survey_date', qgr.response, NULL)) AS 'date_survey_taken',
qgi.entity_id as entity_id,
es.entity_type_id as entity_type_id,
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_family_members_0_to_14', qgr.response, NULL)) AS 'Q1',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_all_family_members_6_to_14_in_school', qgr.response, NULL)) AS 'Q2',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_household_location', qgr.response, NULL)) AS 'Q3',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_owns_radio', qgr.response, NULL)) AS 'Q4',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_house_floors', qgr.response, NULL)) AS 'Q5',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_money_from_abroad', qgr.response, NULL)) AS 'Q6',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_salaried_employment', qgr.response, NULL)) AS 'Q7',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_how_many_plots', qgr.response, NULL)) AS 'Q8',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_house_roof', qgr.response, NULL)) AS 'Q9',
GROUP_CONCAT(if(q.nickname = 'ppi_haiti_2006_owns_pigs', qgr.response, NULL)) AS 'Q10'
FROM question_group_response qgr, question_group_instance qgi, question_group qg, sections_questions sq, questions q, event_sources es
WHERE qgr.question_group_instance_id = qgi.id and qgr.sections_questions_id = sq.id and sq.question_id = q.question_id and qgi.question_group_id = qg.id and qg.title="PPI Haiti 2006" and qgi.event_source_id = es.id
GROUP BY question_group_instance_id) as answers