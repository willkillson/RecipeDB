#Used by getShoppingList()
#userID - 5D825141
#cupboardID - 6701C02E
#Note - This will be a parameterized call.

select STORES.ingredientID
from USER, STORES
where USER.userID="5D825141"
AND USER.cupboardID = "6701C02E"
AND STORES.cupboardID = USER.cupboardID;