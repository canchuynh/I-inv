Features
	Web service
	*	Uses web service to export SQLite database, import is not yet implemented.
	*	To export: Menu (triple dots) > Export
	*	To view SQL webserver go to: http://cssgate.insttech.washington.edu/~canhuynh/I-Inv/list.php?cmd=inventory
	
	Provides Sign In
	*	Uses Google Sign in (and out), user can pick from preexisting account on device or add new account.
	*	To sign in: Menu (triple dots) > Sign In and select account or add new account.
	*	Test account (You can use any gmail):
			User: I.inv.Test@gmail
			Pass: TeamSixTest
	*	To sign out: Menu (triple dots) > Sign Out
	
	One other feature: SQLite Database
	*	Use SQLite Database to store, view, update, and remove items into inventory. 
	*	Stores ID (Primary key, hidden), Name, Value, Condition, and Description.
	*	To store new item: Add FAB > enter (valid) information > Add Item
	*	To view item: Click on any item on list.
	*	To update item: View an item (see above) > Edit Item Details > enter updated information > SAVE CHANGES
	*	To remove item: View an item (see above) > Delete This Item > OK

Notes:
Export to SQL server is not tied to any account (all users of the app share it).
	SQL server's table is dropped before every export for phase 1 for reasons state above.
Search is not yet implemented
More fields will be added to each item later.


Use Cases Implemented
	Use Case 1: Login
	Use Case 2: Add an Item
	Use Case 4: View Details of an Item
	Use Case 5: Edit an item
	Use Case 6: Delete an item
	Use Case 8: Export Inventory
	Use Case 10: Logout