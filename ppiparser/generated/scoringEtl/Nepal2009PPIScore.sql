select answers.survey_id, 0 as points_version, date(str_to_date(answers.date_survey_taken, '%d/%m/%Y')) as date_survey_taken, answers.entity_id, answers.entity_type_id,

(
case
when answers.Q1='Four or more' then 0
when answers.Q1='Three' then 6
when answers.Q1='Two' then 12
when answers.Q1='One' then 16
when answers.Q1='None' then 28
end +
case
when answers.Q2='Not all' then 0
when answers.Q2='No children ages 5 to 12' then 2
when answers.Q2='All' then 5
end +
case
when answers.Q3='No' then 0
when answers.Q3='Yes' then 8
end +
case
when answers.Q4='No' then 0
when answers.Q4='No female head/spouse' then 5
when answers.Q4='Yes' then 8
end +
case
when answers.Q5='Straw/thatch, wood/planks, earth/mud, or other' then 0
when answers.Q5='Tiles/slate' then 4
when answers.Q5='Galvanized iron, or concrete/cement' then 10
end +
case
when answers.Q6='No toilet' then 0
when answers.Q6='Household non-flush, communal latrine, household flush (connected to municipal sewer), or household flush (connected to septic tank)' then 7
end +
case
when answers.Q7='Open fireplace, other, or no data' then 0
when answers.Q7='Mud stove, smokeless stove, or kerosene/gas stove' then 5
end +
case
when answers.Q8='None' then 0
when answers.Q8='One' then 6
when answers.Q8='Two or more' then 13
end +
case
when answers.Q9='No' then 0
when answers.Q9='Yes' then 5
end +
case
when answers.Q10='No' then 0
when answers.Q10='Yes' then 11
end
) 
as ppi_score
from
(SELECT
qg.id as survey_id,
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_survey_date', qgr.response, NULL)) AS 'date_survey_taken',
qgi.entity_id as entity_id,
es.entity_type_id as entity_type_id,
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_family_members_0_to_12', qgr.response, NULL)) AS 'Q1',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_family_members_5_to_12_in_school', qgr.response, NULL)) AS 'Q2',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_family_members_at_private_school', qgr.response, NULL)) AS 'Q3',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_female_head_can_read', qgr.response, NULL)) AS 'Q4',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_house_roof', qgr.response, NULL)) AS 'Q5',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_toilet_type', qgr.response, NULL)) AS 'Q6',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_stove_type', qgr.response, NULL)) AS 'Q7',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_music_players', qgr.response, NULL)) AS 'Q8',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_owns_transportation', qgr.response, NULL)) AS 'Q9',
GROUP_CONCAT(if(q.nickname = 'ppi_nepal_2009_owns_television', qgr.response, NULL)) AS 'Q10'
FROM question_group_response qgr, question_group_instance qgi, question_group qg, sections_questions sq, questions q, event_sources es
WHERE qgr.question_group_instance_id = qgi.id and qgr.sections_questions_id = sq.id and sq.question_id = q.question_id and qgi.question_group_id = qg.id and qg.title="PPI Nepal 2009" and qgi.event_source_id = es.id
GROUP BY question_group_instance_id) as answers