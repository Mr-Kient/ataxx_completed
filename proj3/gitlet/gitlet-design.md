# Gitlet Design Document
Author: Darren Wang

## 1. Classes and Data Structures

The program mainly consists of the following java classes.
* `Main`
* `File`
* `Object`
* `Command`\
and
* `Diff`
* `Utils`
* `GitletException`\
which are already provided.

### a. `Main`

This class is designed as the main component of this program utilizing
all other classes to fulfill the function that the program is designed
to achieve, like version control, file submission, etc. It is basically
like the **.exe** files for Windows OS or **.app** files for MacOS if 
you are not familiar with the weird computer terminologies.

`Main` takes in your command as its input argument, and will validate 
it within the class to avoid some nonsense commands, e.g., do you like
Prof. Hilfinger, write the hw for me, etc.

### b. `File`

This class is designed for representing any necessary file for recording
its related information and files, e.g., BRANCH, HEAD, etc. It works 
like cache in the computers so that your current working status would
not be missing after closing or restarting the program.

### c. `Object`

This class is designed for representing any object in the system. 
It could be branches, head, the file you want to save, etc.

### d. `Command`

This class represents all the commands that the program could read. 
Imagine you are using Git, and this class is the things you can type
in the CMD or Terminal that the program could receive and then execute,
e.g., add, commit, log, status, etc.

### e. `Diff`

A comparison of two sequences of strings. After executing setSequences 
to initialize the data, methods allow computing the longest common sequences 
and differences in the form of edits needed to convert one sequence to 
the next.
--P. N. Hilfinger

### f. `Utils`

The associated utilities for certain functions to work, e.g., filing
sentences (writeContent, readContent, join, etc.). Made by P. N. Hilfinger.

### g. `GitletException`

General exception indicating a Gitlet error.  For fatal errors, the result
of .getMessage() is the error message to be printed.
--P. N. Hilfinger

## 2. Algorithms

### a. `Main`

#### i. void Main(String[])

The main algorithm. It takes in the commands as a string list argument and 
then call appropriate funtions and classes to fulfill its designed functionality.

#### ii. void validate()

The helper function for the Main function. It is used for validating the input command
to see if it is executable. A GitletException would be thrown if the command is not executable.

### b. `File`

#### i. File object

#### ii. File head

#### iii. File branch

### c. `Object`

#### i. String type

#### ii. String timestamp

#### iii. String content

#### iv. String message

### d. `Command`

#### i. void commit(String)

#### ii. void add(String)

#### iii. void log()

#### iv. void status()

#### v. void branch(String)

#### vi. void reset(String)

#### vii. void checkout(String)

### e. `Diff`

### f. `Utils`

### g. `GitletException`

## 3. Persistence

We used java `File` class to create and save necessary files or cache.
All the files related would be saved to the root/.gitlet workspace
for further usage.

## 4. Design Diagram

![](C:\Users\Darren\Documents\Documents\Berkeley\Year_1_Sem_2\CS61B\repo\proj3\gitlet-design.png)

