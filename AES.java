import java.io.*;


public class AES {

	public static int[][] cryptMatrix = new int[4][4]; 
	public static int[][] roundKey;
	public static int rconIdx = 0; 
	public static int keyIdx = 0; 
	static boolean decrypt;

	public static int [][] sBox = new int [] []{
		{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
		{0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
		{0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
		{0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
		{0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
		{0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
		{0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
		{0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
		{0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
		{0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
		{0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
		{0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
		{0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
		{0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
		{0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
		{0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
	};
	public static int [][] inverseSBox = new int [] [] {
		{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
		{0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
		{0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
		{0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
		{0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
		{0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
		{0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
		{0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
		{0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
		{0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
		{0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
		{0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
		{0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
		{0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
		{0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
		{0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
	};
	final static int[] LogTable = {
		0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
		100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
		125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
		101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
		150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
		102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
		126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
		43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
		175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
		44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
		127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
		204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
		151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
		83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
		68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
		103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};
	final static int[] AlogTable = {
		1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
		95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
		229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
		83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
		76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
		131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
		181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
		254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
		251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
		195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
		159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
		155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
		252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
		69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
		18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
		57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};
	public static int[][] rcon = {
		{0x01, 00, 00, 00}, {0x02, 00, 00, 00}, {0x04, 00, 00, 00}, {0x08, 00, 00, 00}, 
		{0x10, 00, 00, 00}, {0x20, 00, 00, 00}, {0x40, 00, 00, 00}, {0x80, 00, 00, 00}, 
		{0x1b, 00, 00, 00}, {0x36, 00, 00, 00}, {0x6c, 00, 00, 00}, {0xd8, 00, 00, 00}, 
		{0xab, 00, 00, 00}, {0x4d, 00, 00, 00}, {0x9a, 00, 00, 00}, {0x2f, 00, 00, 00}, 
		{0x5e, 00, 00, 00}, {0xbc, 00, 00, 00}, {0x63, 00, 00, 00}, {0xc6, 00, 00, 00}, 
		{0x97, 00, 00, 00}, {0x35, 00, 00, 00}, {0x6a, 00, 00, 00}, {0xd4, 00, 00, 00}, 
		{0xb3, 00, 00, 00}, {0x7d, 00, 00, 00}, {0xfa, 00, 00, 00}, {0xef, 00, 00, 00}, 
		{0xc5, 00, 00, 00}, {0x91, 00, 00, 00}, {0x39, 00, 00, 00}, {0x72, 00, 00, 00}, 
		{0xe4, 00, 00, 00}, {0xd3, 00, 00, 00}, {0xbd, 00, 00, 00}, {0x61, 00, 00, 00}, 
		{0xc2, 00, 00, 00}, {0x9f, 00, 00, 00}, {0x25, 00, 00, 00}, {0x4a, 00, 00, 00}, 
		{0x94, 00, 00, 00}, {0x33, 00, 00, 00}, {0x66, 00, 00, 00}, {0xcc, 00, 00, 00}, 
		{0x83, 00, 00, 00}, {0x1d, 00, 00, 00}, {0x3a, 00, 00, 00}, {0x74, 00, 00, 00}, 
		{0xe8, 00, 00, 00}, {0xcb, 00, 00, 00}, {0x8d, 00, 00, 00}, {0x01, 00, 00, 00}, 
		{0x02, 00, 00, 00}, {0x04, 00, 00, 00}, {0x08, 00, 00, 00}, {0x10, 00, 00, 00}, 
		{0x20, 00, 00, 00}, {0x40, 00, 00, 00}, {0x80, 00, 00, 00}, {0x1b, 00, 00, 00}, 
		{0x36, 00, 00, 00}, {0x6c, 00, 00, 00}, {0xd8, 00, 00, 00}, {0xab, 00, 00, 00}, 
		{0x4d, 00, 00, 00}, {0x9a, 00, 00, 00}, {0x2f, 00, 00, 00}, {0x5e, 00, 00, 00}, 
		{0xbc, 00, 00, 00}, {0x63, 00, 00, 00}, {0xc6, 00, 00, 00}, {0x97, 00, 00, 00}, 
		{0x35, 00, 00, 00}, {0x6a, 00, 00, 00}, {0xd4, 00, 00, 00}, {0xb3, 00, 00, 00}, 
		{0x7d, 00, 00, 00}, {0xfa, 00, 00, 00}, {0xef, 00, 00, 00}, {0xc5, 00, 00, 00}, 
		{0x91, 00, 00, 00}, {0x39, 00, 00, 00}, {0x72, 00, 00, 00}, {0xe4, 00, 00, 00}, 
		{0xd3, 00, 00, 00}, {0xbd, 00, 00, 00}, {0x61, 00, 00, 00}, {0xc2, 00, 00, 00}, 
		{0x9f, 00, 00, 00}, {0x25, 00, 00, 00}, {0x4a, 00, 00, 00}, {0x94, 00, 00, 00}, 
		{0x33, 00, 00, 00}, {0x66, 00, 00, 00}, {0xcc, 00, 00, 00}, {0x83, 00, 00, 00}, 
		{0x1d, 00, 00, 00}, {0x3a, 00, 00, 00}, {0x74, 00, 00, 00}, {0xe8, 00, 00, 00}, 
		{0xcb, 00, 00, 00}, {0x8d, 00, 00, 00}, {0x01, 00, 00, 00}, {0x02, 00, 00, 00}, 
		{0x04, 00, 00, 00}, {0x08, 00, 00, 00}, {0x10, 00, 00, 00}, {0x20, 00, 00, 00}, 
		{0x40, 00, 00, 00}, {0x80, 00, 00, 00}, {0x1b, 00, 00, 00}, {0x36, 00, 00, 00}, 
		{0x6c, 00, 00, 00}, {0xd8, 00, 00, 00}, {0xab, 00, 00, 00}, {0x4d, 00, 00, 00}, 
		{0x9a, 00, 00, 00}, {0x2f, 00, 00, 00}, {0x5e, 00, 00, 00}, {0xbc, 00, 00, 00}, 
		{0x63, 00, 00, 00}, {0xc6, 00, 00, 00}, {0x97, 00, 00, 00}, {0x35, 00, 00, 00}, 
		{0x6a, 00, 00, 00}, {0xd4, 00, 00, 00}, {0xb3, 00, 00, 00}, {0x7d, 00, 00, 00}, 
		{0xfa, 00, 00, 00}, {0xef, 00, 00, 00}, {0xc5, 00, 00, 00}, {0x91, 00, 00, 00}, 
		{0x39, 00, 00, 00}, {0x72, 00, 00, 00}, {0xe4, 00, 00, 00}, {0xd3, 00, 00, 00}, 
		{0xbd, 00, 00, 00}, {0x61, 00, 00, 00}, {0xc2, 00, 00, 00}, {0x9f, 00, 00, 00}, 
		{0x25, 00, 00, 00}, {0x4a, 00, 00, 00}, {0x94, 00, 00, 00}, {0x33, 00, 00, 00}, 
		{0x66, 00, 00, 00}, {0xcc, 00, 00, 00}, {0x83, 00, 00, 00}, {0x1d, 00, 00, 00}, 
		{0x3a, 00, 00, 00}, {0x74, 00, 00, 00}, {0xe8, 00, 00, 00}, {0xcb, 00, 00, 00}, 
		{0x8d, 00, 00, 00}, {0x01, 00, 00, 00}, {0x02, 00, 00, 00}, {0x04, 00, 00, 00}, 
		{0x08, 00, 00, 00}, {0x10, 00, 00, 00}, {0x20, 00, 00, 00}, {0x40, 00, 00, 00}, 
		{0x80, 00, 00, 00}, {0x1b, 00, 00, 00}, {0x36, 00, 00, 00}, {0x6c, 00, 00, 00}, 
		{0xd8, 00, 00, 00}, {0xab, 00, 00, 00}, {0x4d, 00, 00, 00}, {0x9a, 00, 00, 00}, 
		{0x2f, 00, 00, 00}, {0x5e, 00, 00, 00}, {0xbc, 00, 00, 00}, {0x63, 00, 00, 00}, 
		{0xc6, 00, 00, 00}, {0x97, 00, 00, 00}, {0x35, 00, 00, 00}, {0x6a, 00, 00, 00}, 
		{0xd4, 00, 00, 00}, {0xb3, 00, 00, 00}, {0x7d, 00, 00, 00}, {0xfa, 00, 00, 00}, 
		{0xef, 00, 00, 00}, {0xc5, 00, 00, 00}, {0x91, 00, 00, 00}, {0x39, 00, 00, 00}, 
		{0x72, 00, 00, 00}, {0xe4, 00, 00, 00}, {0xd3, 00, 00, 00}, {0xbd, 00, 00, 00}, 
		{0x61, 00, 00, 00}, {0xc2, 00, 00, 00}, {0x9f, 00, 00, 00}, {0x25, 00, 00, 00}, 
		{0x4a, 00, 00, 00}, {0x94, 00, 00, 00}, {0x33, 00, 00, 00}, {0x66, 00, 00, 00}, 
		{0xcc, 00, 00, 00}, {0x83, 00, 00, 00}, {0x1d, 00, 00, 00}, {0x3a, 00, 00, 00}, 
		{0x74, 00, 00, 00}, {0xe8, 00, 00, 00}, {0xcb, 00, 00, 00}, {0x8d, 00, 00, 00}, 
		{0x01, 00, 00, 00}, {0x02, 00, 00, 00}, {0x04, 00, 00, 00}, {0x08, 00, 00, 00}, 
		{0x10, 00, 00, 00}, {0x20, 00, 00, 00}, {0x40, 00, 00, 00}, {0x80, 00, 00, 00}, 
		{0x1b, 00, 00, 00}, {0x36, 00, 00, 00}, {0x6c, 00, 00, 00}, {0xd8, 00, 00, 00}, 
		{0xab, 00, 00, 00}, {0x4d, 00, 00, 00}, {0x9a, 00, 00, 00}, {0x2f, 00, 00, 00}, 
		{0x5e, 00, 00, 00}, {0xbc, 00, 00, 00}, {0x63, 00, 00, 00}, {0xc6, 00, 00, 00}, 
		{0x97, 00, 00, 00}, {0x35, 00, 00, 00}, {0x6a, 00, 00, 00}, {0xd4, 00, 00, 00}, 
		{0xb3, 00, 00, 00}, {0x7d, 00, 00, 00}, {0xfa, 00, 00, 00}, {0xef, 00, 00, 00}, 
		{0xc5, 00, 00, 00}, {0x91, 00, 00, 00}, {0x39, 00, 00, 00}, {0x72, 00, 00, 00}, 
		{0xe4, 00, 00, 00}, {0xd3, 00, 00, 00}, {0xbd, 00, 00, 00}, {0x61, 00, 00, 00}, 
		{0xc2, 00, 00, 00}, {0x9f, 00, 00, 00}, {0x25, 00, 00, 00}, {0x4a, 00, 00, 00}, 
		{0x94, 00, 00, 00}, {0x33, 00, 00, 00}, {0x66, 00, 00, 00}, {0xcc, 00, 00, 00}, 
		{0x83, 00, 00, 00}, {0x1d, 00, 00, 00}, {0x3a, 00, 00, 00}, {0x74, 00, 00, 00}, 
		{0xe8, 00, 00, 00}, {0xcb, 00, 00, 00}, {0x8d, 00, 00, 00}};

	public static void main(String[] args) {

		cryptMatrix = setUpArray(args[2], 4); 
		int[][] cipher = setUpArray (args[1], 8);
		roundKey = setUpArray (args[1], 15*4);
		decrypt = args[0].equals("d"); 

		//java AES option keyFile inputFile
		System.out.println("Original Matrix Given:"); 
		printMatrix(cryptMatrix)
		;
		System.out.println("Original CipherKey: ");
		printMatrix(cipher);  

		int i = 0; 
		keyIdx = 8;

		while (keyIdx < roundKey[0].length){
			updateRoundKey();
		}

		System.out.println("RoundKey Expanded: "); 
		for (i = 0; i<roundKey.length; i++)
		{
			for (int j =0; j<roundKey[i].length; j++)
			{
				String aHex = Integer.toHexString(roundKey[i][j]); 
				if (aHex.length() == 1) aHex = "0"+ aHex; 
				System.out.print(aHex + " ");


				if (((j+1)%4) == 0) System.out.print(" "); 
			}
			System.out.println();
		}
		System.out.println();


		if (!decrypt){
			run(); 
		}
		else{
			sBox = inverseSBox; 
			runD(); 
		}

	}

	private static void runD(){
		keyIdx =roundKey[0].length -4;
		int i = 13; 

		System.out.println("After addRoundKey(" + 14+ ")"); 
		addRoundKey(); 
		printMatrix(cryptMatrix); 
		
		System.out.println("InverseShiftRows Results: " );
		inverseShiftRows();
		printMatrix(cryptMatrix); 
		
		System.out.println("InverseSubBytes Results: " );
		subBytes();
		printMatrix(cryptMatrix);
		
		while (i >0) {
			runRoundsD(i);
			i--; 
		} 

		System.out.println("After addRoundKey(" + 0+ ")"); 
		addRoundKey(); 
		printMatrix(cryptMatrix); 
		
		System.out.println("The Cipher Text: "); 
		for (i = 0; i<cryptMatrix.length; i++)
		{
			for (int j =0; j<cryptMatrix[i].length; j++)
			{
				String aHex = Integer.toHexString(cryptMatrix[i][j]); 
				if (aHex.length() == 1) aHex = "0"+ aHex; 
				System.out.print(aHex + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void runRoundsD(int i) {
		
		System.out.println("After addRoundKey(" + i+ ")");
		addRoundKey(); 
		printMatrix(cryptMatrix); 
		
		System.out.println("InverseMixColumns Results: " );
		mixColumns(); 
		printMatrix(cryptMatrix); 
		
		System.out.println("InverseShiftRows Results: " );
		inverseShiftRows();
		printMatrix(cryptMatrix);
		
		System.out.println("InverseSubBytes Results: " );
		subBytes();
		printMatrix(cryptMatrix); 
	}

	private static void run(){
		keyIdx =0;
		int i = 1; 

		System.out.println("After addRoundKey(" + 0+ ")"); 
		addRoundKey(); 
		printMatrix(cryptMatrix); 

		while (i < 14) {
			runRounds(i);
			i++; 
		} 


		System.out.println("SubBytes Results: " );
		subBytes();
		printMatrix(cryptMatrix);

		System.out.println("ShiftRows Results: " );
		shiftRows();
		printMatrix(cryptMatrix); 

		System.out.println("After addRoundKey(" + (i)+ ")");
		addRoundKey(); 
		printMatrix(cryptMatrix); 

		System.out.println("The Cipher Text: "); 
		for (i = 0; i<cryptMatrix.length; i++)
		{
			for (int j =0; j<cryptMatrix[i].length; j++)
			{
				String aHex = Integer.toHexString(cryptMatrix[i][j]); 
				if (aHex.length() == 1) aHex = "0"+ aHex; 
				System.out.print(aHex + " ");
			}
			System.out.println();
		}
		System.out.println();

	}

	private static void runRounds(int i) {
		System.out.println("SubBytes Results: " );
		subBytes();
		printMatrix(cryptMatrix);

		System.out.println("ShiftRows Results: " );
		shiftRows();
		printMatrix(cryptMatrix);

		System.out.println("MixColumns Results: " );
		mixColumns(); 
		printMatrix(cryptMatrix);  

		System.out.println("After addRoundKey(" + i+ ")");
		addRoundKey(); 
		printMatrix(cryptMatrix); 
	}

	private static int[][] setUpArray(String file, int size) {
		BufferedReader in;
		int [][] making = new int[4][size]; 
		try {
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line !=null)
			{	

				int col = 0;
				int row = 0;

				if (line.length() > 64)
				{
					System.out.println("This line has too many characters. Moving on.");
					line = in.readLine();
					continue;
				}

				for (int i = 0; line != null && i<line.length()-1; i=i +2)
				{
					if (row == 4)
					{
						row = 0;
						col++;
					}
					int hex;
					try {
						hex = Integer.decode("0x"+line.substring(i,i+2));
						making[row][col] = hex;
					} catch (NumberFormatException e) {
						System.out.println("MISFORMATTED TEXT ERROR ON "+ line.substring(i,i+2));
						System.exit(1);
					}


					row++; 
				}
				line = in.readLine();
			}
			in.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return making; 
	}

	//print matrix
	//used for debugging purposes
	public static void printMatrix (int[][] matrix) {

		for (int i = 0; i<matrix[0].length; i++)
		{
			for (int j =0; j<matrix.length; j++)
			{
				String aHex = Integer.toHexString(matrix[j][i]); 
				if (aHex.length() == 1) aHex = "0"+ aHex; 
				System.out.print(aHex);
			}
		}
		System.out.println("\n"); 

	}
	//subBytes
	public static void subBytes(){
		int row = 0;
		int col = 0;
		int firstdigit = 0;
		int lastdigit = 0;
		int parameter = 0;
		while (parameter < (cryptMatrix.length*cryptMatrix[0].length))
		{
			if (col == 4)
			{
				col = 0;
				row++;
			}
			firstdigit = (cryptMatrix[row][col]>>4)&0xF;
			lastdigit = cryptMatrix[row][col]&0xF;

			cryptMatrix[row][col] = sBox[firstdigit][lastdigit];
			col++;
			parameter++;
		}
	}

	//shiftRows
	public static void shiftRows(){

		//temporary variables with number indicating original column
		int temp1=0;
		int temp2=0;
		int temp3=0;
		int temp4=0;

		//first row stays the same
		//second row: 2nd to 1st, 3rd to 2nd, 4th to 3rd, 1st goes to 4th
		temp1=cryptMatrix[1][0];
		temp2=cryptMatrix[1][1];
		temp3=cryptMatrix[1][2];
		temp4=cryptMatrix[1][3];
		cryptMatrix[1][0]= temp2;
		cryptMatrix[1][1]= temp3;
		cryptMatrix[1][2]= temp4;
		cryptMatrix[1][3]= temp1;


		//third row: 3rd to 1st, 4th to 2nd, 1st to 3rd, 2nd to 4th
		temp1=cryptMatrix[2][0];
		temp2=cryptMatrix[2][1];
		temp3=cryptMatrix[2][2];
		temp4=cryptMatrix[2][3];
		cryptMatrix[2][0]= temp3;
		cryptMatrix[2][1]= temp4;
		cryptMatrix[2][2]= temp1;
		cryptMatrix[2][3]= temp2;


		//fourth row: 4th to 1st, 1st to 2nd, 2nd to 3rd, 3rd to 4th
		temp1=cryptMatrix[3][0];
		temp2=cryptMatrix[3][1];
		temp3=cryptMatrix[3][2];
		temp4=cryptMatrix[3][3];
		cryptMatrix[3][0]= temp4;
		cryptMatrix[3][1]= temp1;
		cryptMatrix[3][2]= temp2;
		cryptMatrix[3][3]= temp3;

	}
	public static void inverseShiftRows(){

		//temporary variables with number indicating original column
		int temp1=0;
		int temp2=0;
		int temp3=0;
		int temp4=0;

		//first row stays the same
		//second row: 2nd to 1st, 3rd to 2nd, 4th to 3rd, 1st goes to 4th
		temp1=cryptMatrix[1][0];
		temp2=cryptMatrix[1][1];
		temp3=cryptMatrix[1][2];
		temp4=cryptMatrix[1][3];
		cryptMatrix[1][0]= temp4;
		cryptMatrix[1][1]= temp1;
		cryptMatrix[1][2]= temp2;
		cryptMatrix[1][3]= temp3;


		//third row: 3rd to 1st, 4th to 2nd, 1st to 3rd, 2nd to 4th
		temp1=cryptMatrix[2][0];
		temp2=cryptMatrix[2][1];
		temp3=cryptMatrix[2][2];
		temp4=cryptMatrix[2][3];
		cryptMatrix[2][0]= temp3;
		cryptMatrix[2][1]= temp4;
		cryptMatrix[2][2]= temp1;
		cryptMatrix[2][3]= temp2;


		//fourth row: 1st to 2nd, 2nd to 3rd, 3rd to 4th,
		temp1=cryptMatrix[3][0];
		temp2=cryptMatrix[3][1];
		temp3=cryptMatrix[3][2];
		temp4=cryptMatrix[3][3];
		cryptMatrix[3][0]= temp2;
		cryptMatrix[3][1]= temp3;
		cryptMatrix[3][2]= temp4;
		cryptMatrix[3][3]= temp1;

	}

	//mixColumns
	public static void mixColumns() {

		if (decrypt){
			for (int i = 0; i < cryptMatrix.length; i++){
				invMixColumn2(i); 
			}
		}
		else{
			for (int i = 0; i < cryptMatrix.length; i++){
				mixColumn2(i); 
			}}
	}

	public static void mixColumn2 (int c) {
		// This is another alternate version of mixColumn, using the 
		// logtables to do the computation.

		int a[] = new int[4];

		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
			a[i] = cryptMatrix[i][c];

		// This is exactly the same as mixColumns1, if 
		// the mul columns somehow match the b columns there.
		cryptMatrix[0][c] = (mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
		cryptMatrix[1][c] = (mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
		cryptMatrix[2][c] = (mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
		cryptMatrix[3][c] = (mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
	} // mixColumn2

	public static void invMixColumn2 (int c) {
		int a[] = new int[4];

		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
			a[i] = cryptMatrix[i][c];

		cryptMatrix[0][c] = (mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
		cryptMatrix[1][c] = (mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
		cryptMatrix[2][c] = (mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
		cryptMatrix[3][c] = (mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
	} // invMixColumn2

	private static int mul (int a, int b) {
		byte b2 = (byte) (b & 0xFF); 
		int inda = (a < 0) ? (a + 256) : a;
		int indb = (b2 < 0) ? (b2 + 256) : b2;

		if ( (a != 0) && (b != 0) ) {
			int index = (LogTable[inda] + LogTable[indb]);
			int val = (AlogTable[ index % 255 ] );
			return val;
		}
		else 
			return 0;
	} // mul

	//addRoundKey
	public static void addRoundKey(){

		for (int i = 0; i < cryptMatrix[0].length; i++){
			for (int j = 0; j < cryptMatrix.length; j++){
				cryptMatrix[j][i] = cryptMatrix[j][i] ^ roundKey[j][i+keyIdx]; 
			} 
		}

		if (decrypt) keyIdx -= 4; 
		else keyIdx +=4; 
	}

	//updates the RoundKey
	public static void updateRoundKey(){
		//word = last column "used" - col 3, 7, 11...
		//rotate word
		int[] rotWord = new int[4];
		if (keyIdx%8 ==0)
			rotWord = rotateWord(); 
		else{
			for (int help = 0; help < 4; help++){
				rotWord[help] = roundKey[help][keyIdx-1]; 
			}}

		//subbytes word
		rotWord = subBytesWord(rotWord);

		//XOR first column (0,with word and RCON
		//then XOR next column with result
		int[] rconWord = rcon[rconIdx]; 
		int end = (keyIdx+8 > roundKey[0].length) ? keyIdx+4 : keyIdx+8; 

		for (int i = keyIdx; i < end; i++){
			for (int j = 0; j < roundKey.length; j++){
				if (i == keyIdx+4 && j ==0){rotWord = subBytesWord(rotWord);}

				if (i == keyIdx)  roundKey[j][i] = roundKey[j][i-8] ^ rotWord[j] ^ rconWord[j];
				else roundKey[j][i] = roundKey[j][i-8] ^ rotWord[j]; 
				rotWord[j] = roundKey[j][i];  
			} 
		}

		rconIdx++; 
		if (rconIdx == rcon.length) rconIdx = 0; 

		keyIdx += 8;		//increment to next block in array
	}

	private static int[] subBytesWord(int[] rotWord) {
		int row = 0;

		while (row < (rotWord.length))
		{
			int firstdigit = (rotWord[row]>>4)&0xF;
			int lastdigit = rotWord[row]&0xF;

			rotWord[row] = sBox[firstdigit][lastdigit]; 
			row++; 
		}
		return rotWord;
	}

	private static int[] rotateWord(){
		int[] rotWord = new int[4];

		//get last word used in last block
		//rotate top item to bottom.
		for (int i = 0; i < 4; i++) {
			if (i <3) rotWord[i] = roundKey[i+1][keyIdx-1]; 
			else rotWord[i] = roundKey[0][keyIdx-1]; 
		}

		return rotWord; 
	}
}
