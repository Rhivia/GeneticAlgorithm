/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gahelloworld;

import java.util.Random;

/**
 *
 * @author adalberto.bcpereira
 */
public class Chromosome implements Comparable<Chromosome> {
	private final String gene;
	private final int fitness;
	
	/** The target gene, converted to an array for convenience. */
	private static final char[] TARGET_GENE = "Hello, world!".toCharArray();
        
	/** Convenience randomizer. */
	private static final Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * Default constructor.
	 *
	 * @param gene The gene representing this <code>Chromosome</code>.
	 */
	public Chromosome(String gene) {
		this.gene    = gene;
		this.fitness = calculateFitness(gene);
	}
	
	/**
	 * Method to retrieve the gene for this <code>Chromosome</code>.
	 *
	 * @return The gene for this <code>Chromosome</code>.
	 */
	public String getGene() {
		return gene;
	}
	
	/**
	 * Method to retrieve the fitness of this <code>Chromosome</code>.  Note
	 * that a lower fitness indicates a better <code>Chromosome</code> for the
	 * solution.
	 *
	 * @return The fitness of this <code>Chromosome</code>.
	 */
	public int getFitness() {
		return fitness;
	}
	
	/**
	 * Helper method used to calculate the fitness for a given gene.  The
	 * fitness is defined as being the sum of the absolute value of the 
	 * difference between the current gene and the target gene.
	 * 
	 * @param gene The gene to calculate the fitness for.
	 * 
	 * @return The calculated fitness of the given gene.
	 */
	private static int calculateFitness(String gene) {
            int fitness = 0;
            char[] arr  = gene.toCharArray();
            for (int i = 0; i < arr.length; i++) {
		fitness += Math.abs(((int) arr[i]) - ((int) TARGET_GENE[i]));
            }

            return fitness;
	}

	/**
	 * Method to generate a new <code>Chromosome</code> that is a random
	 * mutation of this <code>Chromosome</code>.  This method randomly
	 * selects one character in the <code>Chromosome</code>s gene, then
	 * replaces it with another random (but valid) character.  Note that
	 * this method returns a new <code>Chromosome</code>, it does not
	 * modify the existing <code>Chromosome</code>.
	 * 
	 * @return A mutated version of this <code>Chromosome</code>.
	 */
        public Chromosome mutate() {
		char[] genes    = gene.toCharArray();
		int mutation    = rand.nextInt(genes.length);
		genes[mutation] = letters();

		return new Chromosome(String.valueOf(genes));
	}
        
        public char letters() {
            // arr[idx]    = (char) ((arr[idx] + delta) % 122);
            
            if ( rand.nextInt() % 2 == 0 ) {
                return (char)((rand.nextInt() % 25) + 64); // Uppercase letters
            } else if ( rand.nextInt() % 2 == 0 ) {
                return (char)((rand.nextInt() % 25) + 97); // Lowercase letters
            } else {
                return (char)((rand.nextInt() % 14) + 33); // Special characters
            }
        }
        
	/**
	 * Method used to mate this <code>Chromosome</code> with another.  The
	 * resulting child <code>Chromosome</code>s are returned.
	 * 
	 * @param mate The <code>Chromosome</code> to mate with.
	 * 
	 * @return The resulting <code>Chromosome</code> children.
	 */
	public Chromosome[] mate(Chromosome mate) {
            
            // Convert the genes to arrays to make thing easier.
            char[] arr1  = gene.toCharArray();
            char[] arr2  = mate.gene.toCharArray();

            // Select a random pivot point for the mating
            int pivot    = rand.nextInt(arr1.length);

            // Provide a container for the child gene data
            char[] child1 = new char[gene.length()];
            char[] child2 = new char[gene.length()];

            // Copy the data from each gene to the first child.
            for(int i = 0; i <= pivot; i++){
                child1[i] = arr1[i];
            }
            for(int i = pivot; i < gene.length(); i++){
                child1[i] = arr2[i];
            }
            
            for(int i = 0; i <= pivot; i++){
                child2[i] = arr2[i];
            }
            for(int i = pivot; i < gene.length(); i++){
                child2[i] = arr1[i];
            }

            return new Chromosome[] { 
                new Chromosome(String.valueOf(child1)), 
                new Chromosome(String.valueOf(child2))
            }; 
	}
        
        public Chromosome invert() {
            char[] arr  = gene.toCharArray();

            // Select a random pivot point for the mating
            int pivotOne    = rand.nextInt(arr.length);
            int pivotTwo    = rand.nextInt(arr.length);
            
            if(pivotOne > pivotTwo) {
                int aux = pivotOne;
                pivotOne = pivotTwo;
                pivotTwo = aux;
            }
            
            // char[] auxArr = new char[pivotTwo - pivotOne];

            // Copy the data from each gene to the first child.
            for(int i = pivotOne, j = pivotTwo; i < j; i++, j--){
                char aux = arr[i];
                arr[i] = arr[j];
                arr[j] = aux;
            }
            
            // System.out.println("Inverted: " + String.valueOf(arr));
            
            return new Chromosome (String.valueOf(arr)); 
	}
	
	/**
	 * A convenience method to generate a randome <code>Chromosome</code>.
	 * 
	 * @return A randomly generated <code>Chromosome</code>.
	 */
	/* package */ static Chromosome generateRandom() {
		char[] arr = new char[TARGET_GENE.length];
		for (int i = 0; i < arr.length; i++) {
                    arr[i] = (char) (rand.nextInt(90) + 32);
		}

		return new Chromosome(String.valueOf(arr));
	}

	/**
	 * Method to allow for comparing <code>Chromosome</code> objects with
	 * one another based on fitness.  <code>Chromosome</code> ordering is 
	 * based on the natural ordering of the fitnesses of the
	 * <code>Chromosome</code>s.  
	 */
	@Override
	public int compareTo(Chromosome c) {
		if (fitness < c.fitness) {
                    return -1;
		} else if (fitness > c.fitness) {
                    return 1;
		}
		
		return 0;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Chromosome)) {
			return false;
		}
		
		Chromosome c = (Chromosome) o;
		return (gene.equals(c.gene) && fitness == c.fitness);
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {		
		return new StringBuilder().append(gene).append(fitness).toString().hashCode();
	}
}