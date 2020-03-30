package com.onaple.brawlator.probability;


import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Singleton
public class ProbabilityFetcher {

    private final Random random;

    public ProbabilityFetcher() {
        this.random = new Random();
    }

    public <T extends Probable> Optional<T> fetcher(List<T> pool){
        double randomValue = random.nextDouble();
        Iterator<T> iterator = pool.iterator();
        double accumulatedProbabilities = 0;
        while (iterator.hasNext()) {
             T current = iterator.next();
            accumulatedProbabilities += current.getProbability();
            if (randomValue <= accumulatedProbabilities) {
               return Optional.of(current);
            }
        }
        return Optional.empty();
    }

}
