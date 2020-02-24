#Used by displayShoppingList()
#userID - 38CA10BA
#cupboardID - CB1FD927
#recipeID - 0925E411
#Note - This will be a parameterized call.

SELECT Ingredient.ingredientID, Ingredient.name FROM Ingredient, Lists, Adds WHERE 
Ingredient.ingredientID=Lists.ingredientID AND 
Lists.recipeid=adds.recipeid AND 
adds.recipeID = '0925E411' AND 
lists.ingredientID NOT in (select STORES.ingredientID
from USER, STORES
where USER.userID="38CA10BA"
AND USER.cupboardID = "CB1FD927"
AND STORES.cupboardID = USER.cupboardID);