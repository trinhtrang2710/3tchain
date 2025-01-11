package src;

import java.util.ArrayList;

public class Tchain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 4; // Mining difficulty

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = StringUtils.getDifficultyString(difficulty);

        // First check - Genesis block validation
        if (blockchain.isEmpty()) {
            System.out.println("Empty blockchain");
            return false;
        }

        Block genesis = blockchain.get(0);
        if (!genesis.previousHash.equals("0")) {
            System.out.println("Invalid genesis block");
            return false;
        }

        // Loop through blockchain to check hashes
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // Check if the block's data is null or empty
            if (currentBlock.getData() == null || currentBlock.getData().trim().isEmpty()) {
                System.out.println("Block " + i + " has invalid data");
                return false;
            }

            // Check block hash integrity
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Block " + i + " has invalid hash");
                System.out.println("Registered hash: " + currentBlock.hash);
                System.out.println("Calculated hash: " + currentBlock.calculateHash());
                return false;
            }

            // Verify hash pointer to previous block
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Block " + i + " has invalid previous hash reference");
                System.out.println("Registered previous hash: " + currentBlock.previousHash);
                System.out.println("Actual previous hash: " + previousBlock.hash);
                return false;
            }

            // Verify proof of work
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("Block " + i + " hasn't been mined properly");
                return false;
            }

            // Verify timestamp is logical
            if (currentBlock.getTimeStamp() <= previousBlock.getTimeStamp()) {
                System.out.println("Block " + i + " has invalid timestamp");
                return false;
            }
        }

        System.out.println("Blockchain validation successful");
        return true;
    }

    // Helper method to print validation errors
    private static void printValidationError(String message, int blockIndex) {
        System.out.println("Validation Error at Block " + blockIndex + ": " + message);
    }

    // Method to get chain status
    public static String getChainStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Chain Length: ").append(blockchain.size()).append("\n");
        status.append("Current Difficulty: ").append(difficulty).append("\n");
        status.append("Latest Block Hash: ").append(blockchain.get(blockchain.size() - 1).hash).append("\n");
        status.append("Is Valid: ").append(isChainValid());
        return status.toString();
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static void main(String[] args) {
        // Add first block (Genesis)
        System.out.println("Mining genesis block...");
        addBlock(new Block("Genesis Block", "0"));

        // Add second block
        System.out.println("\nMining second block...");
        addBlock(new Block("Second Block", blockchain.get(blockchain.size()-1).hash));

        // Add third block
        System.out.println("\nMining third block...");
        addBlock(new Block("Third Block", blockchain.get(blockchain.size()-1).hash));

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        // Display the blockchain
        System.out.println("\nThe blockchain:");
        for (int i = 0; i < blockchain.size(); i++) {
            Block block = blockchain.get(i);
            System.out.println("Block #" + i);
            System.out.println("Hash: " + block.hash);
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println("Data: " + block.getData());
            System.out.println("Timestamp: " + block.getTimeStamp());
            System.out.println("Nonce: " + block.getNonce());
            System.out.println();
        }
    }
}