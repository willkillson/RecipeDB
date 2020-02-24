#Used by verifyUser()
#userID - 5EC07057
#password - D639F761
#cupboardID - 3A834DBA
#Note - This will be a parameterized call.

select userID, cupboardID, cartID
from USER
where USER.userID="5EC07057"
AND USER.password = "D639F761";