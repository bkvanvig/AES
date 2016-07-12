AES Crypto


Semantics


File named AES.java

Executed by:        java AES option keyFile inputFile

option: e or d for encryption or decryption

if ‘e’ is given, inputFile is encrypted with the key from keyFile and program outputs an encrypted file by adding extension ‘.enc’ to inputFile

if ‘d’ is given, inputFile is decrypted with keyFile and outputs file by adding extension ‘dec’ to the inputFile
keyFile is a single line of 64 hex characters

inputFile is a file containing lines of plaintext

Algorithm steps


subBytes: for each byte in the array, use it’s value as an index into a fixed element lookup table (available online) and replace it’s value with the value at index on table.

shiftRows: for Ri (row i in table) shift i bytes. Circular shifts. Do not affect the bytes themselves

mixColumns: replace the column by it’s value * a fixed 4x4 matrix of integers (Galois Field). NOTE: inverse is multiplied by a different table.

addRoundkey: XOR the state with 128-bit round key derived from the original key K by a recursive process

Assignment


AES-256

128-bit input

256-bit key

14 rounds


if file contains:
-non-hex characters, skip
-less than 64 characters, pad with 0’s on the right

Measure how many Mb/sec you can encrypt and then how many you can decrypt. Compare speeds

Rounds

Do all four steps rounds 1-9, 10th ignore mix columns
