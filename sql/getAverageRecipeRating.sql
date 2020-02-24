#Used by getAverageRecipeRating()
#recipeID - 0925E411
#Note - This will be a parameterized call.

select AVG(ADDS.rating)
from ADDS
where ADDS.recipeID = "0925E411";
