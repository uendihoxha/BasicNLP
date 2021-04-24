# basicNLP
- PROBLEM DESCRIPTION 

For this assignment we are required to perform some basic natural language processing using
Java. The program should construct a language model, classify a specific text file and select
the language whose model is similar to the specified text file. The program must contain a
folder locating the mistery.txt file, which is the file to be classified; and subfolders containing
the text files of languages that are going to be used for classification.

- REQUIREMENTS

1. At least two subfolders for each language.
2. Loops are not allowed; for iteration needs use streams only.
3. Language folders, text files must be processed concurrently.
4. Apply synchronization techniques for thread-safety & correct behavior of the
program.

- INSTRUCTIONS

• N-gram model (sequence of n linguistic items)
• Document Distance (cosine similarity)


- SOLUTION DESCRIPTION 

The language model is constructed from the text files. Consequently, I have chosen 3
languages to classify mistery.txt: Albanian (al), German (de) and English (en). Subfolders
can be found at data folder; each of these subfolders contain text files of the specific
language.

N-GRAM MODEL

N-gram models are widely used in statistical natural language processing; it is the simplest
model that assigns probabilities to sentences and sequences of words, the n-gram (Rizvi,
2019). A 1-gram (or unigram) is a one-word sequence. A 2-gram (or bigram) is a two-word
sequence of words and a 3-gram (or trigram) is a three-word sequence of words (See figure
1.1).

![ImgName](https://github.com/uendihoxha/basicNLP/blob/master/images/n-gram%20model.PNG)

An N-gram language model predicts the probability of a given N-gram within any sequence
of words in the language. For a given text, we can compute a list of all the n-grams. The size
of the n-grams (n) must be specified in the arguments, which in this case n=2. 

COSINE SIMILARITY

Given two vectors of attributes, A and B, the cosine similarity, cos(θ), is represented using a dot
product and magnitude as:

![ImgName](https://github.com/uendihoxha/basicNLP/blob/master/images/formula.PNG)

This metric is frequently used when trying to determine similarity between two documents. In
this similarity metric, the attributes (or words, in the case of the documents) is used as a
vector to find the normalized dot product of the two documents.
By determining the cosine similarity, the user is effectively trying to find cosine of the angle
between the two objects. For cosine similarities resulting in a value of 0, the documents do
not share any attributes (or words) because the angle between the objects is 90 degrees. 


- OUTPUT 

![ImgName](https://github.com/uendihoxha/basicNLP/blob/master/images/output1.PNG)

Output 1. Classifying mystery.txt as AL

![ImgName](https://github.com/uendihoxha/basicNLP/blob/master/images/output2.PNG)

Output 2. Classifying mystery.txt as EN

![ImgName](https://github.com/uendihoxha/basicNLP/blob/master/images/output3.PNG)

Output 3. Classifying mystery.txt as DE


- How to run the program

1. User should specify the folder path as first argument, otherwise the program
exists: \Users\User\Desktop\UendiHoxha-assign1-nlp\data
2. JDK 8 or late
