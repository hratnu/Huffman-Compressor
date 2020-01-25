Harshil Ratnu

Four .java files included: BinaryIn, BinaryOut, Huffman, HuffmanSubmit


HuffmanSubmit : This file implements the encode and decode methods.

Encode: This method takes the input, output, and frequency file as arguments.
It creates a Hashmap to store each character with its frequency and a Linkedlist which stores all the characters. While theinput file is not empty, the characters are read and noted with its frequency and added to the Hashmap.

To Generate the Frequency file: A hashmap named entry is generated which gets the entry from the original hashmap and prints the character in the form of an 8bit binary string with its frequency using the write method.

To build the tree: A priority queue is constructed and a for loop iterates through the Hashmap to add nodes to the priority queue. The priority queue removes two nodes with the lowest frequency and makes a parent third node with the two removed nodes as its left and right child nodes. 
The parent node's frequency is the sum of the frequencies of the two child nodes. The final node left is called Htree.

A new Hashmap HCode gives every character a code. Until it reaches the leaf, O is assigned to every left traversal and 1 to every right traversal. To create the encoded file, every bit with "0" value is assigned false and "1" value is assigned true.
The output file is then flushed.

Decode:
A bufferedReader is used to read the frequencyfile. The String input is split and the string is represented as a character in ASCII code.
This character is put in the hashmap with its frequency. A try and catch exception is thrown.

Rebuilding the tree: A priority queue tree is created with every element of the Hashmap as its node. The priority queue removes two nodes with the lowest frequency and makes a parent third node with the two removed nodes as its left and right child nodes.: 

Decoding: A boolean Linked list is created. The input file is read and stored as boolean in the Linkedlist. A for loop runs through this linkedlist. If the boolean value was true it traverses right and if it is false it traverses left until it reaches the leaf. It then uses the write method to wite the character value of the final node.

Main Method: Tests out the encoding and decoding using the given sample files.