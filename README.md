# Blockchain

It's a simple implementation of blockchain which distributed and decentralized ledger that stores data like transactions, that is publicly shared across all the nodes of its network.

## Implemented Features
- Core Blockchain.
- Create Transactions.
- Mining Blocks.
- Dynamic P2P for multiple users in same network.
- Proof of work.

## Some Explanations
It contains 4 parts: blockchain, mining, transactions and wallet.
#### Blockchain
With hash and last hash, we connect each block, and make the connection stable.
#### Mining
- Add transactions to the blockchain, need to be confirmed, solving a proof of work algorithm. 
- Once one miner solve, broadcast it to others, miner can add this block and other miners will verify.
#### Transactions
- It carries a certain data like the sender , the receiver of funds and mount of funds to be transferred.
- A signature that proves the owner of the address is the one sending this transaction and that the data hasn’t been changed.
#### Wallet
- Store the private and public key of wallet's owner.
- Store the balance of the wallet's owner.

