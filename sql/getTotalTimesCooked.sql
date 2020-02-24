#Used by getTotalTimesCookedInSystem()
#recipeID - 0925E411
#Note - This will be a parameterized call.

select SUM(ADDS.timesCooked)
from ADDS
where ADDS.recipeID = "0925E411";