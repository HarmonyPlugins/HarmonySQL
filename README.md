# Harmony SQL

## Description

Harmony SQL is a Graphical User Interface for MySQL inside of Minecraft. It allows server admins to Create / Remove Databases, Create / Remove tables, Add/Remove/Alter Columns and Select Rows all while in game.

## Configuration

```config.yml``` is responsible for entering the MySQL server credentials as well as the security method for gaining access.
<details>
   <summary>config.yml</summary>
  
	mysql:
 	username: "user"
 	password: "pass"
 	host: "127.0.0.1"
 	port: 3306

	security:
 	password: false
 	console: false
 	passphrase: "1234"
</details>

```language.yml``` is reponsible for all the messages in the plugin alongside the prefix.
<details>
   <summary>language.yml</summary>
  
	prefix: "&f[&bHarmony&f]"

	messages: 
 	admin:
 	 console_execute: "&fSorry, but this command can only be run by a &bPlayer&f."
  	 console_code: "&b%player%&f's code is &l&b%code%"
  	 config_reloaded: "&fConfig reloaded successfully."
  	 custom_query: "&fType in your &bcommand&f in chat."
  	 create_database: "&fEnter the name of the &bdatabase&f in chat."
  	 create_table: "&fEnter the name of the &btable&f in chat."
 	 create_column: "&fEnter the name of the &bcolumn&f in chat."
  	 first_column: "&fIn order to create the table, you'll need to create a &bcolumn&f."
  	 column_size: "&fEnter the &bsize&f of your column."
  	 enter_condition: "&fQuery: \n\n&b%query%&f\n\n Enter your new condition in chat(or type &bnone&f to cancel)."
	player:
  		no_access: "&fSorry, but this command requires the &bharmonysql.admin&f permission."
  		no_password: "&fYou must type in the &bpassphrase&f to proceed. (/harmonysql password)"
  		invalid_password: "&fSorry, but the passphrase you entered is &binvalid&f."
  		code_sent: "&fYou need to enter the confirmation code sent to the &bConsole&f."```
</details>


