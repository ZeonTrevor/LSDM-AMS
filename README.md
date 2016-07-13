# LSDM-AMS
## Description
### Distinct Hashtags
The implementation takes as input the path to the dataset, length of the stream and total number of hash functions divided into hash groups (numHashGroups * numHashFunctionsInGroup). Multiple hash functions are used to improve the estimate of the number of distinct items in the stream based on the approach described in [section 4.4.3](http://infolab.stanford.edu/~ullman/mmds/ch4.pdf) of [Mining Massive Datasets](http://www.mmds.org/) book.
### kth Moments
This implementation estimates the value of the kth moment given the path of the dataset, length of stream, number of random variables to consider and kth moment to be estimated using the AMS streaming algorithm described in [section 4.5](http://infolab.stanford.edu/~ullman/mmds/ch4.pdf) of [Mining Massive Datasets](http://www.mmds.org/) book.

## Build Details
The project is compiled with:
```
java version 7
```

## Running the script
The parameters for the script are as follows:
### Distinct Items

1. "1" to select estimation of distinct Items
2. Path to the input dataset
3. Length of stream (n)
4. Number of hash groups
5. Number of hash functions in each hash group

Following is an example:
```
./run2.sh 1 ebola.json.gz 2000 5 2
```
Also you can run the jar file directly using:
```
java -jar ASM.jar ebola.json.gz 2000 5 2
```

### kth Moment
1. "2" to select estimation of kth moment
2. Path to the input dataset
3. Length of stream (n)
4. Number of random variables
5. kth moment to estimate

Following is an example:
```
./run2.sh 2 ebola.json.gz 10000 10 2
```

## References
1. The space complexity of approximating the frequency moments [AMS96](http://dl.acm.org/citation.cfm?id=237823)
2. Chapter 4. Mining Data Streams [Mining Massive Datasets](http://infolab.stanford.edu/~ullman/mmds/ch4.pdf)
