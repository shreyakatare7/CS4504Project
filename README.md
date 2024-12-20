# Parallel and Distributed Computing Project

This is a project required in my parallel and distributed computing class. It uses a hybrid system of parallel computing and the client-server paradigm to do the Strassen's Matrix multiplication. 

## Description
The project requires full implementation of Strassen’s Matrix Multiplication with the existing Java code from the  irst part. Speci ically, the client process takes a series of matrix data and sends it to the server process through the server-router to implement matrix-matrix multiplications. The client process will be using matrices of integers to be multiplied using Matrix-Chain-Multiplication (MCM) along with the previously mentioned Strassen’s algorithm. Most of the implementation done in this project relies on the server-side. It is responsible for using a binary tree to multiply a varying number of matrices using a varying number of threads/cores until a single collector-core sends the  inal product back to the client process. It also calculates runtime, speedup, and efficiency, sending this data back to the client as well.
