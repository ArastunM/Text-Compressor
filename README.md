## Text Compressor
Text Compressor is a java based application used to compress and decompress 
given text files on command

An average of 45% copmression size reduction is maintained


## Prerequisites
[Java version 15.0.1](https://openjdk.java.net/projects/jdk/15/) 
was used for development


## Installation
The project only utilizes java built-in packages


## Getting Started
Before the project is run, it should be ensured 
that the necessary classes/files are within appropriate
directories

The files can be downloaded from the [GitHub repository](https://github.com/ArastunM/Text-Compressor.git)

The directory should look as below:

```
+-- README.md
+-- LICENSE.txt
+-- src
|   +-- HuffmanCompress.class
|   +-- HuffmanCompress.java
|   +-- HuffmanDecompress.class
|   +-- HuffmanDecompress.java
|   +-- Node.class
|   +-- Node.java
|   +-- TextFile.class
|   +-- TextFile.java
|   +-- Tree.class
|   +-- Tree.java
|   +-- TreeNode.class
+-- datasets
|   +-- sample.txt (text files to compress)
+-- javadoc
```

### How to compress text files using Text Compressor
1. Open command line interface and navigate to the directoy of the java class
```
>> cd ...\Text Compression\src
```

2. Run the HuffmanCompress.class
```
>> java HuffmanCompress
```

3. Enter the full path of the text file to be compressed
Ideally the text file should be located inside "datasets" folder
```
>> ...\Text Compression\datasets\sample.txt
```
Or (provided current file structure is kept)
```
>> ..\datasets\sample.txt
```

### How to decompress text files using Text Compressor
1. Follow step 1. above

2. Run the HuffmanDecompress.class
```
>> java HuffmanDecompress
```

3. Enter the full path of the text file to be decompressed
```
>> ...\Text Compression\datasets\sample_compressed.txt
```
Or (provided current file structure is kept)
```
>> ..\datasets\sample_compressed.txt
```

NOTE: If name of the compressed file is to be changed 
so should the name of sample_compressed_list


## Used Classes
The program consists of classes given below:
- **[HuffmanCompress.class]** - 
used to compress the given text file
- **[HuffmanDecompress.class]** - 
used to decompress the given text file
- **[Node.class]** - 
used to handle Node object
- **[TreeNode.class]** - 
used to handle TreeNode object, extends Node class
- **[TextFile.class]** - 
used to handle text file related operations
- **[Tree.class]** - 
used to build a Huffman Tree


## Details
- Author - Arastun Mammadli
- License - [MIT](LICENSE.txt)

**Access link to [GitHub repository](https://github.com/ArastunM/Text-Compressor.git)**
