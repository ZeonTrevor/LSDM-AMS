# LSDM-ASM
## Description
This project implements the ASM streaming algorithm to compute the number of distinct items given a twitter stream of data.
The implementation takes as input the path to the dataset, length of the stream and total number of hash functions divided into hash groups (numHashGroups * numHashFunctionsInGroup = totalHashFunctions). Multiple hash functions are used to improve the estimate of the number of distinct items in the stream.

## Running the script
The parameters for the script are as follows:

1. Path to the input dataset
2. Length of stream (n)
3. Number of hash groups
4. Number of hash functions in each hash group

Following is an example:
```
java -jar ASM.jar ebola.json.gz 2000 5 2
```
