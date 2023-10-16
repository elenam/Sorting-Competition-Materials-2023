# Sorting-Competition-Materials-2023
Materials and results for the UMN Morris CSci 3501 sorting competition, Fall 2023

# Table of contents
* [Goal of the competition](#goal)
* [The data](#data)
* [How is the data generated](#generating)
* [How do you need to sort the data](#sortingRules)
* [Setup for sorting](#setup)
* [Submision deadlines](#deadlines)
* [Scoring](#scoring)

## Goal of the competition <a name="goal"></a>

The Sorting Competition is a multi-lab exercise on developing the fastest sorting algorithm for a given type of data. By "fast" we mean the actual running time and not the Big-Theta approximation. The solutions are developed in Java and will be ran on a single processor.

## The data  <a name="data"></a>

You will be sorting fixed length integers (the program reads them as Strings to preserve the leading zeros, i.e. to keep the format as 0012 instead of 12 when the length is 4). The integers will be sorted as follows:
   * By the sum of all unique prime factors of the numbers in increasing order.
   * If the two numbers have the same sum of the prime factors, they are ordered by value in decreasing order.

For example:
   * 1024 is before 1000 since the sum of prime factors of 1024 is 2, and the sume of prime factors of 1000 is 7.
   * 0121 is before 0011 since their sum of prime factors is 11 for both, and 121 > 11, so they are in decreasing order.
   * We consider both 0 and 1 to have zero prime factors. 0001 is before 0000 since their sum of prime factors is 0, and 1 > 0.

The file [Group00.java](src/Group00.java) provides a Comparator that implements this comparison and provides some tests. Please
consult it as needed. However, note that this is a *very* slow implementation. You can use it for small examples to test correctness, 
but it's too slow on actual data files to be of any use. Because of this I provided the .class file (but not the source code) for a 
faster reference implementation. It's in the [bin](bin/) folder and consists of two class file: Group0.class and Group0$Data.class. 
Download these classes. You can run them directly by navigating to the bin directory typing ```java Group0``` followed by the input 
file name (make sure it exists; use a relative path from bin) and the output file name (if no path specified then will be created in bin).

Once the data is sorted, it is written out to the output file, also one number per line, ordered according to the comparator. 
The file [small_out.txt](small_out.txt) has the results of sorting [small.txt](small.txt). The larger example data is [data.txt](data.txt), 
and the result of sorting it is [data_out.txt](data_out.txt) 

## Setup for sorting <a name="setup"></a>

The file [Group0.java](src/Group0.java) provides a template for the setup for your solution. Your class will be called `GroupN`, where `N` is the group number that is assigned to your group. The template class runs the sorting method once before starting the timing so that [JVM warmup](https://www.ibm.com/developerworks/library/j-jtp12214/index.html) takes place outside of the timed sorting. It also pauses for 10ms before the actual test to let any leftover I/O or garbage collection to finish. Since the warmup and the actual sorting are done on the same array (for no reason other than simplicity), the array is cloned from the same input data. 

The data reading, the array cloning, the warmup sorting, and writing out the output are all outside of the timed portion of the method, and thus do not affect the total time. 

You may **not** use any **global variables** that **depend on your data**. You may, however, have global constants that are initialized to fixed values (no computation!) before the data is being read and stay the same throughout the run. These constants may be arrays of no more than 100 `long` numbers or equivalent amount of memory. For instance, if you are storing an array of objects that contain two `long` fields, you can only have 50 of them. 
We consider one `long` to be the same as two `int` numbers, so you can store an array of 200 `int` numbers. 
If you are allocating strings, note that strings use 16 bits (half of the size of an `int`) for *each* character and 16 bytes (128 bits) for the memory reference and other info for the string *itself*.  
If in doubt about specific cases, please discuss them with me. 

The method in the [Group0.java](src/Group0.java) files that you may modify is the `sort` method. It must take the array of Strings. 
The method can sort in place (thus be `void`) or return type of the method can be what it is now or return the result in an array of another type.
If you are returning an array, the following rules have to be followed:
* Your `sort` method return type needs to be changed to whatever  array you are returning. Consequently you would need to change the call in `main` to store the resulting array. 
* Your return type has to be an array (not an array list!) and it has to have one element per element of the original array. That element (or its field) must be printed as is into the result, no processing should be needed before printing. Use of `printf` in formatting is ok. 
For example, you may create your own class (I will call it `Data` as an example) that has the `int` value of a number plus some other fields. Then you will be returning an array of `Data`. 
You may not, however, create an array of just binary representations of all numbers, return that array, and convert back to decimal upon printing.   
* If you are returning a different type of an array, such as `Data`, you need to supply a method to write out your resulting array into a file. The method will access the `int` value field using a `get` method or directly (if it's accessible) and write it to a file. 
The file has to be exactly the same as in the prototype implementation; they will be compared using `diff` system command. 

If you are not changing the return type, you don't need to modify anything other than `sort` method and any methods/classes called from it. 

Even though you are not modifying anything other than the `sort` method, you still need to submit your entire class: copy the template, rename the Java class to your group number, and change the`sort` method. You may use supplementary classes, just don't forget to submit them. Make sure to add your names in comments when you submit. 

### Other restrictions ###
   * Any memory allocated by your sorting method and any methods called from it must be allocated only for the duration of one round of sorting. All of the data must be recomputed on a subsequent sorting. Be especially careful with static methods, classes, or variables - they are computed once for all instances. Make sure that there is nothing left from the JVM warmup run that could be used in the actual sorting. 
   * Your program will be executed on a single core (see details below), so even if you are using multithreading all the threads will be executed on the same core. However, use of multithreading is allowed, as long as you spawn no more than 4 threads and the number of threads is always the same and doesn't depend on the data.  
     
As always, definitely ask questions if you are not sure if your code is following the rules. 

## Scoring <a name="scoring"></a>

The programs are tested on a few (between 1 and 3) data sets. For each data set each group's program is run three times, the median value is recorded. The groups are ordered by their median score for each data file and assigned places, from 1 to N. 

The final score is given by the sum of places for all data sets. If the sum of places is equal for two groups, the sum of median times for all the runs resolves the tie. So if one group was first for one data set and third for the other one (2 sets total being run), it scored better than a group that was third for the first data set and second for the other. However, if one group was first for the first set and third for the other one, and the second group was second in both, the sum of times determines which one of them won since the sum of places is the same (1 + 3 = 2 + 2). 

If a program has a compilation or a runtime error, doesn't sort correctly, or prints anything other than the total time in milliseconds, it gets a penalty of 1000000ms for that run. 

## System specs <a name="specs"></a>

The language used is Java that's installed in the CSci lab.

I will post a script for running the competition (with a correctness check) with the results of the first preliminary competition, but for now a couple of things to know: run your program out of `/tmp` directory to avoid overhead of communications with the file server, and pin your program to a single core, i.e. run it like this:
``taskset -c 0 java GroupN``

##  Submision deadlines <a name="deadlines"></a>

The first preliminary competition will be run on Thursday October 19th in the lab (due Wednesday Oct 18th at the end of the day). The purpose of it is mostly to check the correctness and to get a sense for your timing. 

The second preliminary round will be in the lab on Monday October 23rd (due Monday Oct 23rd at 11:30am) 

The final competition will be on Thursday October 26th in the lab (due Wed Oct 25th)

The dates for other related assignments (code review and presentations) will be announced later. 

