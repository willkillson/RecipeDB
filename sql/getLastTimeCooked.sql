#Used by getLastTimeCooked()
#recipeID - 0925E411
#Note - This will be a parameterized call.
#will return the last time a recipe was cooked by any user.

select MAX(ADDS.lastCooked)
from ADDS
where ADDS.recipeID = "0925E411";