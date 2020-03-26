/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eesignatures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 *
 * Author Karlis with code made by Luecx
 */
public class Network {
    private double[][] output;
    private double[][][] weights;
    private double[][] bias;
    
    private double[][] errorSignal;
    private double[][] outputDerivative;
    
    public final int[] netLayerSizes;
    public final int inputSize;
    public final int outputSize;
    public final int networkSize;
    
//    First signature - OG mine
//    int width = 100;
//    int height = 75;
//    Second signature - Luse
    int width = 190;
    int height = 75;
    
    int[] photo = new int[(width*height)];
    
    public Network(int... netLayerSizes) {
        this.netLayerSizes = netLayerSizes;
        this.inputSize = netLayerSizes[0];
        this.networkSize = netLayerSizes.length;
        this.outputSize = netLayerSizes[networkSize-1];
        
        this.output = new double[networkSize][];
        this.weights = new double[networkSize][][];
        this.bias = new double[networkSize][];
        
        this.errorSignal = new double[networkSize][];
        this.outputDerivative = new double[networkSize][];
        
        for(int i = 0; i < networkSize; i++){
            this.output[i] = new double[netLayerSizes[i]];
            this.errorSignal[i] = new double[netLayerSizes[i]];
            this.outputDerivative[i] = new double[netLayerSizes[i]];
            
            this.bias[i] = NetworkTools.createRandomArray(netLayerSizes[i], -0.5, 0.7);
            
            if(i > 0){
                weights[i] = NetworkTools.createRandomArray(netLayerSizes[i], netLayerSizes[i-1], -1, 1);
            }
        }
    }
    
    public double[] calculate(double... input){
        if(input.length != this.inputSize) return null;
        this.output[0] = input;
        for(int layer = 1; layer < networkSize; layer++){
            for(int neuron = 0; neuron < netLayerSizes[layer]; neuron++){
                
                double sum = bias[layer][neuron];
                for(int prevNeuron = 0; prevNeuron < netLayerSizes[layer-1]; prevNeuron++){
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
                outputDerivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
            }
        }
        return output[networkSize-1];
    }

    public void train(double[] in, double[] target, double rate){
        if(in.length != inputSize || target.length != outputSize) return;
        calculate(in);
        backpropError(target);
        adjustWeights(rate);
    }
    
    public double MSE(double[] input, double[] target) {
        if(input.length != inputSize || target.length != outputSize) return 0;
        calculate(input);
        double v = 0;
        for(int i = 0; i < target.length; i++) {
            v += (target[i] - output[networkSize-1][i]) 
                * (target[i] - output[networkSize-1][i]);
        }
        return v / (2d * target.length);
    }
    
    public void backpropError(double[] target){
        for(int neuron = 0; neuron < netLayerSizes[networkSize-1]; neuron++){
            errorSignal[networkSize-1][neuron] = (output[networkSize-1][neuron] - target[neuron]) 
                    * outputDerivative[networkSize-1][neuron];
        }
        for(int layer = networkSize - 2; layer > 0; layer--){
            for(int neuron = 0; neuron < netLayerSizes[layer]; neuron++){
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < netLayerSizes[layer+1]; nextNeuron++){
                    sum += weights[layer+1][nextNeuron][neuron] * errorSignal[layer+1][nextNeuron];
                }
                this.errorSignal[layer][neuron] = sum * outputDerivative[layer][neuron];
            }
        }
    }
    
    public void adjustWeights(double rate){
        for(int layer = 1; layer < networkSize; layer++){
            for(int neuron = 0; neuron < netLayerSizes[layer]; neuron++){
                double delta = - rate * errorSignal[layer][neuron];
                bias[layer][neuron] += delta;
                for(int prevNeuron = 0; prevNeuron < netLayerSizes[layer-1]; prevNeuron++){
                    weights[layer][neuron][prevNeuron] += delta * output[layer-1][prevNeuron];
                }
               
            }
        }
    }
    
    public void printOut(Network net, double[] out, double[] input, double[] target){
        System.out.println("Target:");
        System.out.println(Arrays.toString(target));
        System.out.println("Output:");
        System.out.println(Arrays.toString(out));
        System.out.println("MSE is: "+net.MSE(input, target));
    }
    
    private double sigmoid(double x){
        return 1d / (1 + Math.pow(Math.E, -x));
    }
    
    public static void main(String[] args) throws IOException{
        Network net = new Network(190 * 75, 35, 15, 1);
        
        String path = "D:\\Karlis\\Documents\\Skola\\Extended Essay";
        
        EESignatures[] sigArrayReal = new EESignatures[31];
        EESignatures[] sigArrayFake = new EESignatures[31];
        EESignatures[] sigArrayTest = new EESignatures[2];
        
        // Arrays for signature paths
        for(int i = 0; i < sigArrayReal.length; i++){
            //sigArrayReal[i] = new EESignatures(path + "\\Real signatures\\sig-"+(i+1)+".jpg");
            sigArrayReal[i] = new EESignatures(path + "\\Luse real\\Luse-real-"+(i+1)+".jpg");
            sigArrayReal[i].photoFromFile(sigArrayReal[i].getLoc());
            System.out.println((i+1) + " works!");
        }
        for(int i = 0; i < sigArrayFake.length; i++){
            //sigArrayFake[i] = new EESignatures(path + "\\Fake signatures\\fsig-"+(i+1)+".jpg");
            sigArrayFake[i] = new EESignatures(path + "\\Luse fake\\Luse-fake-"+(i+1)+".jpg");
            sigArrayFake[i].photoFromFile(sigArrayFake[i].getLoc());
        }
              
        double[] input = new double[sigArrayReal[0].photo.length];
        double[] target = new double[]{1};
        /*
        // Training with real
        for(int j = 0; j < sigArrayReal.length; j++){
            for(int i = 0; i < input.length; i++) {
                input[i] = sigArrayReal[j].paraugs[i];
            }
            for(int i = 0; i < 1000; i++){
                net.train(input, target, 0.2);
            }
        }
        
        // Training with fake
        double[] out = net.calculate(input);
        System.out.println("Real training");
        net.printOut(net, out, input, target);
        
        target[0] = 0;
        for(int j = 0; j < sigArrayFake.length; j++){
            for(int i = 0; i < input.length; i++) {
                input[i] = sigArrayFake[j].paraugs[i];
            }
            for(int i = 0; i < 230; i++){
                net.train(input, target, 0.2);
            }
        }
        */
        
        // Training with both real and fake
        for(int j = 0; j < sigArrayReal.length * 2; j++){
            if( j%2 == 0){
                target[0] = 1;
                for(int i = 0; i < input.length; i++) {
                    input[i] = sigArrayReal[j/2].photo[i];
                }
                for(int i = 0; i < 62 - j; i++){
                    net.train(input, target, 0.2);
                }
                double[] out = net.calculate(input);
                System.out.println("Training for real "+(j/2+1)+":");
                net.printOut(net, out, input, target);
            }
            if( j%2 != 0){
                target[0] = 0;
                for(int i = 0; i < input.length; i++) {
                    input[i] = sigArrayFake[j/2].photo[i];
                }
                for(int i = 0; i < 62 - j; i++){
                    net.train(input, target, 0.2);
                }
                double[] out = net.calculate(input);
                System.out.println("Training for fake "+(j/2+1)+":");
                net.printOut(net, out, input, target);
            }
        }
        double[] out = net.calculate(input);
        System.out.println("Training:");
        net.printOut(net, out, input, target);
        /*
        out = net.calculate(input);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Fake training");
        net.printOut(net, out, input, target);
        */
        
        target[0] = 1;
        //sigArrayTest[1] = new EESignatures(path + "\\Real signatures\\sig-33.jpg");
        sigArrayTest[1] = new EESignatures(path + "\\Luse real\\Luse-real-32.jpg");
        sigArrayTest[1].photoFromFile(sigArrayTest[1].getLoc());
        for(int i = 0; i < input.length; i++) {
            input[i] = sigArrayTest[1].photo[i];
        }
        out = net.calculate(input);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Real testing");
        net.printOut(net, out, input, target);
        
        double[] temp = new double[7500];
        for(int i=0; i < temp.length; i++) {
            temp[i] = input[i];
        }
        
        target[0] = 0;
        //sigArrayTest[0] = new EESignatures(path + "\\Fake signatures\\fsig-33.jpg");
        sigArrayTest[0] = new EESignatures(path + "\\Luse fake\\Luse-fake-32.jpg");
        sigArrayTest[0].photoFromFile(sigArrayTest[0].getLoc());
        for(int i = 0; i < input.length; i++) {
            input[i] = sigArrayTest[0].photo[i];
        }
        out = net.calculate(input);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Fake testing");
        net.printOut(net, out, input, target);
        
        // Testing if inputs changed
        for(int i = 0; i < temp.length; i++) {
            if(temp[i] != input[i]){
                System.out.println("Different arrays");
                break;
            }
        }
    }
}
