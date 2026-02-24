# Lumi User Guide
## Overview
Lumi is a simple task management application you control through a command-line interface (CLI).

If you can type fast, Lumi can help you with task management faster than traditional GUI apps.

## Table of Contents: 
- [Quick Start](#quick-start)
- [Features](#features)
    - [Viewing help: `help`](#viewing-help-help)
    - [Add Todo task: `todo`](#add-todo-task-todo)
    - [Add Deadline task: `deadline`](#add-deadline-task-deadline)
    - [Add Event task: `event`](#add-event-task-event)
    - [List of tasks: `list`](#list-of-tasks-list)
    - [Mark a task: `mark`](#mark-a-task-mark)
    - [Unmark a task: `unmark`](#unmark-a-task-unmark)
    - [Deleting a task: `delete`](#deleting-a-task-delete)
    - [Finding a task: `find`](#finding-a-task-find)
    - [Exiting the program: `bye`](#exiting-the-program-bye)
- [Command Summary](#command-summary-)
- [FAQ](#faq)
- [Known Issues](#known-issues)
- [Collaboration](#collaboration)
  

## Quick Start
1. Ensure that you have Java 17 or above installed in your Computer.
For MAC users, ensure that you have the precise JDK version prescribed 
[here](https://se-education.org/guides/tutorials/javaInstallationMac.html)
2. Download the latest `.jar` file from [here](https://github.com/nicholaslauhy/ip/releases/)
3. Copy the file to the folder that you want to use as the home folder for your Task Manager.
4. Open Terminal(Mac) or Windows Powershell(Windows), `cd` into the folder you put the jar file in, 
and use the `java -jar Lumi.jar` command to run the application.
5. You should see something like <br>`I am your Task Manager! What's your name?`
<br>Enter your name and you should get the following with a personalized message:
```
Hello <NAME IN CAPITAL LETTERS>, I am
 _      _   _   __  __   ___
| |    | | | | |  \/  | |_ _|
| |    | | | | | |\/| |  | |
| |___ | |_| | | |  | |  | |
|_____| \___/  |_|  |_| |___|

I am LUMIIII, ready when you are!
____________________________________________________________
Tasks for <name>:
```
6. Type the command in the command line and press Enter to execute it
   (e.g. typing `help` and pressing Enter will open the help window.)
7. Refer to the [Features](#features) below for details of each command

## Features
## Viewing help: `help`
Shows instructions on how to use and its format.<br>
<b> Format: </b> `help` <br>
<b> Example: </b> `help` <br>
<b> Partial Expected Output: </b>
```
Tasks for nic:
help
____________________________________________________________
Commands you can use:
1) list - shows all tasks
Format : list
Example: list

2) todo - add tasks that do not have deadlines
Format: todo <description>
Example: todo borrow book
```

## Add Todo task: `todo`
Adds a task without a date <br>
<b> Format: </b> `todo DESCRIPTION` <br>
<b> Example: </b> `todo finish homework` <br>
<b> Expected Output: </b>
```
Tasks for nic:
todo finish homework
____________________________________________________________
Sucks to be youuuu!! NEW TASK FOR YOU!!
 [T][ ] finish homework
Now you have 1 problem to deal with...
```

## Add Deadline task: `deadline`
Adds a task with a deadline. <br>
<b> Format: </b> `deadline DESCRIPTION /by DATE` <br>
<b> Example: </b> `deadline submit report /by tomorrow 6pm` <br>
<b> Expected Output: </b>
```
Tasks for nic:
deadline submit report /by tomorrow
____________________________________________________________
Sucks to be youuuu!! NEW TASK FOR YOU!!
 [D][ ] submit report (by: tomorrow)
Now you have 2 problems to deal with...
```

## Add Event task: `event`
Adds a task with a time range <br>
<b> Format: </b> `èvent DESCRIPTION /from START /to END`
<b> Example: </b> `event cs2113 meeting /from 2pm /to 4pm` <br>
<b> Expected Output: </b>
```
Tasks for nic:
event cs2113 meeting /from 2pm /to 4pm
____________________________________________________________
Sucks to be youuuu!! NEW TASK FOR YOU!!
 [E][ ] cs2113 meeting (from: 2pm to: 4pm)
Now you have 3 problems to deal with...
```

## List of tasks: `list`
Displays all tasks <br>
<b> Format: </b> `list` <br>
<b> Example: </b> `list` <br>
<b> Expected Output: </b>
```
Tasks for nic:
list
____________________________________________________________
Here are the tasks in your list:
 1. [T][ ] finish homework
 2. [D][ ] submit report (by: tomorrow)
 3. [E][ ] cs2113 meeting (from: 2pm to: 4pm)
```

## Mark a task: `mark`
Mark a task as completed <br>
<b> Format: </b> `mark TASK_NUMBER`<br>
<b> Example: </b> `mark 1` <br>
<b> Expected Output: </b>
```
Tasks for nic:
mark 1
____________________________________________________________
Good Job! I have marked this task as done:
 [T][X] finish homework
You have 2 tasks to go
```

## Unmark a task: `unmark`
Mark a task as incomplete <br>
<b> Format: </b> `unmark TASK_NUMBER`<br>
<b> Example: </b> `unmark 1` <br>
<b> Expected Output: </b>
```
Tasks for nic:
unmark 1
____________________________________________________________
Oh no! Let me unmark this for you:
 [T][ ] finish homework
You have 3 tasks to go
```

## Deleting a task: `delete`
Deletes a task from the list <br>
<b> Format: </b> `delete TASK_NUMBER`<br>
<b> Example: </b> `delete 1` <br>
<b> Expected Output: </b>
```
Tasks for nic:
delete 1
____________________________________________________________
Okie go on with your other tasks...
 [T][ ] finish homework
You now have 2 tasks in your list.
```

## Finding a task: `find`
Finds task by keyword <br>
<b> Format: </b> `find KEYWORD`<br>
<b> Example: </b> `find book` <br>
<b> Expected Output: </b>
```
Tasks for nic:
find report
____________________________________________________________
Here are the matching tasks in your list:
 1. [D][ ] submit report (by: tomorrow)
```

## Exiting the program: `bye`
Closes Lumi <br>
<b> Format: </b> `bye` <br>
<b> Example: </b> `find book` <br>
<b> Expected Output: </b>
```
Tasks for nic:
bye
____________________________________________________________
Lumi: BYEBYE Have a LUMInous DAY!
```

## Command Summary 
| Action | Format, Examples |
|--------|------------------|
| Add Todo | `todo DESCRIPTION` <br> e.g. `todo finish homework` |
| Add Deadline | `deadline DESCRIPTION /by DATE` <br> e.g. `deadline submit report /by tomorrow 6pm` |
| Add Event | `event DESCRIPTION /from START /to END` <br> e.g. `event meeting /from 2pm /to 3pm` |
| List | `list` |
| Mark | `mark TASK_NUMBER` <br> e.g. `mark 1` |
| Unmark | `unmark TASK_NUMBER` |
| Delete | `delete TASK_NUMBER` <br> e.g. `delete 2` |
| Find | `find KEYWORD` <br> e.g. `find book` |
| Help | `help` |
| Exit | `bye` |

## FAQ
There is currently nothing here! Watch this space for any updates!

## Known Issues
Currently there are no known issues, but if you find anything wrong, 
do drop me an email at [nicholaslauhongyi@gmail.com](mailto:nicholaslauhongyi@gmail.com) 

## Collaboration
If you would like to work with me for future projects, you can
drop me a follow on my socials and DM me! Let's connect! <br>
[LinkedIn](https://www.linkedin.com/in/nicholas-lau-688509292/) |
[Email](mailto:nicholaslauhongyi@gmail.com) |
[Telegram](https://t.me/nicclau) | 
[Github](https://github.com/nicholaslauhy)