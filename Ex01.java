package part01;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

public class Ex01 {
	public static float[][][] trainImages = null;
	public static float[][] trainLabels = null;
	public static float[][][] testImages = null;
	public static float[][] testLabels = null;
	
	public static float[][][] filters = null;
	public static float[][] fcWeights = null;
	public static float[][] fcBiases = null;
	
	public static float[][] inputConvolution;
	public static float[][][] filtersConvolution;
	
	public static float[][][] inputMaxpool;
	public static float[][][] outputMaxpool;
	
	public static float[][] inputFullyConnected;
	public static float[][] outputFullyConnected;
	
	public static float[][][] init_filters(int size) {
		float[][][] result = new float[size][3][3];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					result[i][j][k] = (float)Math.random();
				}
			}
		}
		return result;
	}
	
	public static void zerosVector(float[] input) {
		for (int i = 0; i < input.length; i++) {
			input[i] = 0.0f;
		}
	}
	
	public static void ini_SoftMax(int input, int output) {
		fcWeights = new float[input][output];
		for (int i = 0; i < input; i++) {
			for (int j = 0; j < output; j++) {
				fcWeights[i][j] = ((float)Math.random()) * (1.0f / input);
			}
		}
		fcBiases = new float[1][10];
		zerosVector(fcBiases[0]);
	}
	
	public static float[][][] loadImages(String filename, int batchSize, int imageSize) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[4];
		fis.read(buffer, 0, 4);
		fis.read(buffer, 0, 4);
		int numImages = ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).getInt();
		System.out.printf("numImage is %d", numImages);
		fis.read(buffer, 0, 4);
		fis.read(buffer, 0, 4);
		float[][][] images = new float[batchSize][imageSize][imageSize];
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < imageSize; j++) {
				for (int k = 0; k < imageSize; k++) {
					images[i][j][k] = (float)fis.read() / (float)255.0;
				}
			}
			if (i == 0) {
				printImage(images[i], imageSize, imageSize);
			}
			if (i % 100 == 99) {
				System.out.printf(".%d\n", i + 1);
			}
			else {
				System.out.printf(".");
			}
		}
		fis.close();
		System.out.printf("\n");
		System.out.printf("loadImages completed\n");
		return images;
	}
	
	public static float[][] loadLabels(String filename, int batchSize, int numClasses) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[4];
		fis.read(buffer, 0, 4);
		fis.read(buffer, 0, 4);
		float[][] labels = new float[batchSize][numClasses];
		for (int i = 0; i < batchSize; i++) {
			int label = fis.read();
			labels[i][label] = (float)1.0;
			if (i == 0) {
				System.out.printf("label is %d\n", label);
			}
		}
		fis.close();
		System.out.printf("\n");
		System.out.printf("loadLabels completed\n");
		return labels;
	}
	
	private static void printImage(float[][] image, int rows, int cols) {
		System.out.printf("printImage rows is %d\n", rows);
		System.out.printf("printImage cols is %d\n", cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				float pixel = image[i][j];
				if (pixel > (float)0.5) {
					System.out.printf("#");
				}
				else {
					System.out.printf(" ");
				}
			}
			System.out.printf("\n");
		}
	}
	
	public static float elsumxMatrix(float[][] mat1, float[][] mat2) {
		float sum = 0;
		for (int i = 0; i < mat1.length; i++) {
			for (int j = 0;j < mat2[0].length; j++) {
				sum += mat1[i][j] * mat2[i][j];
			}
		}
		return sum;
	}
	
	public static float[][] convolve3x3 (float[][] image, float[][] filter) {
		inputConvolution = image;
		float[][] result = new float[image.length - 2][image[0].length - 2];
		for (int i = 1; i < image.length - 2; i++) {
			for (int j = 1; j < image[0].length - 2; j++) {
				float[][] conv_region = subMatrix(image, i - 1, i +1, j - 1, j + 1);
				result[i][j] = elsumxMatrix(conv_region, filter);
			}
		}
		return result;
	}
	
	public static float[][][] convolution(float[][] iamge, float[][][] filter) {
		filtersConvolution = filter;
		float[][][] result = new float[8][26][26];
		for (int i = 0; i < filters.length; i++) {
			float[][] res = convolve3x3(iamge, filters[i]);
			result[i] = res;
		}
		return result;
	}
	
	public static float[][] addMatrix(float[][] m1, float[][] m2) {
		float[][] result = new float[m1.length][m1[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {
				result[i][j] = m1[i][j] + m2[i][j];
			}
		}
		return result;
	}
	
	public static float[][] subMatrix(float[][] mat, int r_s, int r_e, int c_s, int c_e) {
		float[][] sub = new float[r_e - r_s + 1][c_e - c_s + 1];
		for (int i = 0; i < sub.length; i++) {
			for (int j = 0; j < sub[0].length; j++) {
				sub[i][j] = mat[r_s + i][c_s + j];
			}
		}
		return sub;
	}
	
	public static float[][] scaleMatrix(float[][] mat, float scale) {
		float[][] scl = new float[mat.length][mat[0].length];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				scl[i][j] = (float)mat[i][j] * scale;
			}
		}
		return scl;
	}
	
	public static void convolution_backprop (float[][][] d_L_d_out, float learning_rate) {
		float[][][] d_L_d_filters = new float[filtersConvolution.length][filtersConvolution[0].length][filtersConvolution[0][0].length];
		for (int i = 1; i < inputConvolution.length - 2; i++) {
			for (int j = 1; j < inputConvolution[0].length - 2; j++) {
				for (int k = 0; k < filtersConvolution.length; k++) {
					float[][] region = subMatrix(inputConvolution, i - 1, i + 1, j - 1, j + 1);
					d_L_d_filters[k] = addMatrix(d_L_d_filters[k], scaleMatrix(region, d_L_d_out[k][i - 1][j - 1]));
				}
			}
		}
		for (int i = 0; i < filtersConvolution.length; i++) {
			filtersConvolution[i] = addMatrix(filtersConvolution[i], scaleMatrix(d_L_d_filters[i], -learning_rate));
		}
	}
	
	public static float[][][] maxPooling(float[][][] dta) {
		inputMaxpool = dta;
		float[][][] result = new float[dta.length][dta[0].length][dta[0][0].length];
		for (int i = 0; i < dta.length; i++) {
			float[][] res = max_pool(dta[i]);
			result[i] = res;
		}
		outputMaxpool = result;
		return result;
	}
	
	public static float[][][] maxpool_backprop(float[][][] d_L_d_out) {
		float[][][] d_L_d_input = new float[inputMaxpool.length][inputMaxpool[0].length][inputMaxpool[0][0].length];
		for (int i = 0; i < outputMaxpool.length; i++) {
			for (int j = 0; j < outputMaxpool[0].length; j++) {
				for (int k = 0; k < outputMaxpool[0][0].length; k++) {
					float[][] region = subMatrix(inputMaxpool[i], j * 2, j * 2 + 1, k * 2, k * 2 + 1);
					for (int l = 0; l < region.length; l++) {
						for (int m = 0; m < region[0].length; m++) {
							if (Math.abs(outputMaxpool[i][j][k] - region[l][m]) < 0.00000001) {
								d_L_d_input[i][j * 2 + m][k * 2 + m] = d_L_d_out[i][j][k];
							}
						}
					}
				}
			}
		}
		return d_L_d_input;
	}
	
	public static float[][] flatten(float[][][] mat) {
		float[][] v = new float[1][mat.length * mat[0].length * mat[0][0].length];
		int l = 0;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				for (int k = 0; k < mat[0][0].length; k++) {
					v[0][l] = mat[i][j][k];
					l++;
				}
			}
		}
		return v;
	}
	
	public static float[][] multMatrix(float[][] m1, float[][] m2) {
		float[][] result = new float[m1.length][m2[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2[0].length; j++) {
				for (int k = 0; k < m1[0].length; k++) {
					result[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return result;
	}
	
	public static float[][] fullyConnected(float[][] input, float[][] weights, float[][] biases) {
		outputFullyConnected = addMatrix(multMatrix(input, weights), biases);
		inputFullyConnected = input;
		return outputFullyConnected;
	}
	
	public static float sumVector(float[][] v) {
		float sum = 0;
		for (int i = 0; i < v[0].length; i++) {
			sum += v[0][i];
		}
		return sum;
	}
	
	public static float[][] expVector(float[][] v) {
		float[][] exp = new float[1][v[0].length];
		for (int i = 0; i < v[0].length; i++) {
			exp[0][i] = (float)Math.exp(v[0][i]);
		}
		return exp;
	}
	
	public static float[][] scaleVector(float[][] v, float scale) {
		float[][] scl = new float[1][v[0].length];
		for (int i = 0; i < v[0].length; i++) {
			scl[0][i] = (float)v[0][i] * scale;
		}
		return scl;
	}
	
	public static float[][] softmax(float[][] input) {
		float[][] totals = expVector(input);
		float inv_activation_sum = 1 / sumVector(totals);
		return scaleVector(totals, inv_activation_sum);
	}
	
	public static float[][] transposeMatrix(float[][] mat) {
		float[][] transpose = new float[mat[0].length][mat.length];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				transpose[j][i] = mat[i][j];
			}
		}
		return transpose;
	}
	
	public static float[][][] reshapeMatrix(float[][] input, int d, int h, int w) {
		float[][][] output = new float[d][h][w];
		int input_index = 0;
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				for (int k = 0; k < w; k++) {
					output[i][j][k] = input[0][input_index];
					input_index++;
				}
			}
		}
		return output;
	}
	
	public static float[][][] fullyConnected_backprop(float[][] d_L_d_out, float learning_rate) {
		float[][] d_L_d_t = new float[1][d_L_d_out[0].length];
		float[][] t_exp = expVector(outputFullyConnected);
		float S = sumVector(t_exp);
		float[][] d_L_d_inputs = null;
		
		for (int i = 0; i < d_L_d_out[0].length; i++) {
			float grad = d_L_d_out[0][i];
			if (grad == 0) {
				continue;
			}
			float[][] d_out_d_t = scaleVector(t_exp, -t_exp[0][i] / (S * S));
			d_out_d_t[0][i] = t_exp[0][i] * (S - t_exp[0][i]) / (S * S);
			
			d_L_d_t = scaleMatrix(d_out_d_t, grad);
			float[][] d_t_d_weight = transposeMatrix(inputFullyConnected);
			float[][] d_t_d_inputs = fcWeights;
			float[][] d_L_d_w = multMatrix(d_t_d_weight, d_L_d_t);
			d_L_d_inputs = multMatrix(d_t_d_weight, d_L_d_t);
			float[][] d_L_d_b = d_L_d_t;
			fcWeights = addMatrix(scaleMatrix(d_L_d_w, -learning_rate), fcWeights);
			fcBiases = addMatrix(scaleMatrix(d_L_d_b, -learning_rate), fcBiases);
		}
		return reshapeMatrix(transposeMatrix(d_L_d_inputs), 8, 13, 13);
	}
	
	public static int maxIndex(float[] array) {
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static void train(int batchSize) throws IOException {
		float ce_loss = 0;
		int accuracy = 0;
		float acc_sum = 0.0f;
		float learn_rate = 0.005f;
		
		float[][] out_l = new float[1][10];
		for (int i = 0; i < batchSize; i++) {
			float[][] pxl = trainImages[i];
			float[][][] out = convolution(pxl, filters);
			out = maxPooling(out);
			float[][] flatOutput = flatten(out);
			float[][] fcOutput = fullyConnected(flatOutput, fcWeights, fcBiases);
			out_l = softmax(fcOutput);
			int correct_label = maxIndex(trainLabels[i]);
			ce_loss += (float) -Math.log(out_l[0][correct_label]);
			accuracy += correct_label == maxIndex(out_l[0]) ? 1 : 0;
			float[][] gradient = new float[1][10];
			zerosVector(gradient[0]);
			gradient[0][correct_label] = -1 / out_l[0][correct_label];
			float[][][] sm_gradient = fullyConnected_backprop(gradient, learn_rate);
			float[][][] mp_gradient = maxpool_backprop(sm_gradient);
			convolution_backprop(mp_gradient, learn_rate);
			if (1 % 100 == 99) {
				System.out.printf(" step : %d, loss : %f, accuracy : %d\n", i, ce_loss / 100.0, accuracy);
				ce_loss = 0;
				acc_sum += accuracy;
				accuracy = 0;
			}
		}
	}
	
	public static void test(int batchSize) throws IOException {
		int label_counter = 0;
		int accuracy = 0;
		float acc_sum = 0.0f;
		
		float[][] out_l = new float[1][10];
		for (int i = 0; i < batchSize; i++) {
			float[][] pxl = testImages[i];
			float[][][] out = convolution(pxl, filters);
			
			out = maxPooling(out);
			
			float[][] flatOutput = flatten(out);
			float[][] fcOutput = fullyConnected(flatOutput, fcWeights, fcBiases);
			out_l = softmax(fcOutput);
			
			int correct_label = maxIndex(testLabels[i]);
			float ce_loss = (float)-Math.log(out_l[0][correct_label]);
			int detected_label = maxIndex(out_l[0]);
			accuracy += correct_label == detected_label ? 1 : 0;
			if (correct_label != detected_label ) {
				
			}
			
			if (i % 100 == 99) {
				System.out.printf("step : %d, loss : %f, accuracy : %d\n", i, ce_loss / 100.0, accuracy);
				ce_loss = 0;
				acc_sum += accuracy;
				accuracy = 0;
			}
		}
		System.out.printf("average accuracy : %f%\n", ((acc_sum * 100) / batchSize));
	}
	
	public static float maxMatrix(float[][] mat) {
		float max = mat[0][0];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				max = max < mat[i][j] ? mat[i][j] : max;
			}
		}
		return max;
	}
	
	public static float[][] max_pool(float[][] img) {
		float[][] pool = new float[img.length / 2][img[0].length / 2];
		for (int i = 0; i < pool.length - 1; i++) {
			for (int j = 0; j < pool[0].length - 1; j++) {
				pool[i][j] = maxMatrix(subMatrix(img, i * 2, i * 2 + 1, j * 2, j * 2 + 1));
			}
		}
		return pool;
	}
	
	public static void main(String[] args) throws IOException {
		int imageSize = 28;
		int numClasses = 10;
		int batchSize = 60000;
		int testBatchSize = 10000;
		
		System.out.printf("Load training data\n");
		String trainImagesFile = "train-images.idx3-ubyte";
		String trainLabelsFile = "train-labels.idx1-ubyte";
		trainImages = loadImages(trainImagesFile, batchSize, imageSize);
		trainLabels = loadLabels(trainLabelsFile, batchSize, numClasses);
		
		filters = init_filters(8);
		ini_SoftMax(13 * 13 * 8,  10);
		
		train(batchSize);
		test(testBatchSize);
	}
}
