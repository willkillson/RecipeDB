#Used by getRecipeInformation()
#recipeID - 0925E411
#Note - This will be a parameterized call.
#will return all the information about a recipe

select RECIPE.name,RECIPE.URL, MAX(ADDS.lastCooked),SUM(ADDS.timesCooked), AVG(ADDS.rating)
from RECIPE, ADDS
where RECIPE.recipeID = "0925E411"
AND ADDS.recipeID = "0925E411"
AND ADDS.recipeID = RECIPE.recipeID;
