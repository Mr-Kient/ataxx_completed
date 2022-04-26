# Gitlet Design Document
Author: Darren Wang

## 1. Classes and Data Structures

The program mainly consists of the following java classes.
* `Main`
* `Files`
* `Objects`
* `Command`
* `Index`\
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

### b. `Files`

This class is designed for representing any necessary file for recording
its related information and files, e.g., BRANCH, HEAD, etc. It works 
like cache in the computers so that your current working status would
not be missing after closing or restarting the program.

### c. `Objects`

This class is designed for representing any object in the system. 
It could be branches, head, the file you want to save, etc.

### d. `Command`

This class represents all the commands that the program could read. 
Imagine you are using Git, and this class is the things you can type
in the CMD or Terminal that the program could receive and then execute,
e.g., add, commit, log, status, etc.

### e. `Index`

### f. `Diff`

A comparison of two sequences of strings. After executing setSequences 
to initialize the data, methods allow computing the longest common sequences 
and differences in the form of edits needed to convert one sequence to 
the next.
--P. N. Hilfinger

### g. `Utils`

The associated utilities for certain functions to work, e.g., filing
sentences (writeContent, readContent, join, etc.). Made by P. N. Hilfinger.

### h. `GitletException`

General exception indicating a Gitlet error.  For fatal errors, the result
of .getMessage() is the error message to be printed.
--P. N. Hilfinger


## 2. Algorithms

### a. `Main`

#### i. void Main(String[])

The main algorithm. It takes in the commands as a string list argument and 
then call appropriate funtions and classes to fulfill its designed functionality.

#### ii. void validateArg(String[] args, int req)

The helper function for the Main function. It is used for validating the input command
to see if it is executable. A GitletException would be thrown if the command is not executable.


### b. `File`

#### i. void initDir()

Initialize working directory.

#### ii. void writeObject(Objects Object)

Write Objects in the OBJECTS directory. Save by the first 2 letters of sha1 (same as Git).

#### iii. boolean writeStagedToIndex(String sha1, String filename)



#### iv. void writeHead(String sha1)

Write / update the current HEAD.

#### v. void updateBranchHead(String branch, String sha1)

Update the sha1 code of the Head of the very branch.

#### vi. void updateRepoFile(File newBlobLoc, String blobHash)

Put the content of given blob into a new one. For checkout use.

* The strategy to save files in this system is to use its sha1 code like Git does.
  * E.g., we have a new file whose sha1 code is 0b2a40250fe9328e751984616...
  * The system would save the file in CWD -> OBJECTS -> 0b -> 2a40250fe9328e751984616...
  * For the "0b" part, we would call it "Head Hash", and for the "2a..." part, we would
    call it "Body Hash".

#### vii. String getHeadHash(String sha1)

Get the head of the sha1 code from the provided sha1. Use for saving files by Head
order in the OBJECTS directory.

#### viii. String getBodyHash(String sha1)

Get the body of the sha1 code from the provided sha1. Use for saving files by
Body order in its HEAD directory.

#### ix. File getObjectsFile(String sha1)

Get the file directory from the provided sha1.

#### x. Objects getObjectsHash(String hash)

Get the Object of the given sha1 hash.

#### xi. String getCurrHead()

Get current Head as a string containing its sha1.

#### xii. Objects getCurrHeadCommit()

Get current commit as an Object.

#### xiii. String getHeadGeneral(String branchHash)

Get the Head as a string containing its sha1 for the given sha1 of the branch.

#### xiv. List<String> pastCommits(String headHash)

Get history of commits under current Branch. Saved as a List.


### c. `Objects`

#### i. String type

#### ii. String timestamp

#### iii. String content

#### iv. String msg


### d. `Command`

#### i. void init()

#### ii. void add(String file)

#### iii. void commit(String msg)

#### iv. void rm(String file)

#### v. void log()

#### vi. void globalLog()

#### vii. void find(String commitmsg)

#### viii. void status()

#### ix. void checkoutPastFile(String sha1, String file)

#### x. void checkoutHeadFile(String file)

#### xi. void checkoutBranch(String branch)

#### xii. void branch(String branch)

#### xiii. void rmBranch(String branch)

#### xiv. void reset(String sha1)

#### xv. void merge(String branch)


### e. `Index`


### f. `Diff`


### g. `Utils`


### h. `GitletException`


## 3. Persistence

We used java `Files` class to create and save necessary files or cache.
All the files related would be saved to the root/.gitlet workspace
for further usage.


## 4. Design Diagram

![](C:\Users\Darren\Documents\Documents\Berkeley\Year_1_Sem_2\CS61B\repo\proj3\gitlet-design.png)

