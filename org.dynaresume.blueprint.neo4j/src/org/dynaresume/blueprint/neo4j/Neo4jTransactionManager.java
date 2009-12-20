package org.dynaresume.blueprint.neo4j;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class Neo4jTransactionManager extends AbstractPlatformTransactionManager
		implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger("tx");
	private NeoService neoService;

	public Neo4jTransactionManager() {
		setNestedTransactionAllowed(false);
	}

	public Neo4jTransactionManager(NeoService neoService) {
		this();
		this.neoService = neoService;
		afterPropertiesSet();
	}

	public void setNeoService(NeoService neoService) {
		this.neoService = neoService;
	}

	@Override
	public void afterPropertiesSet() {
		if (neoService == null) {
			throw new IllegalArgumentException("neoService must be set");
		}
	}

	protected Object doGetTransaction() {
		log.info("Neo4jTransactionManager.doGetTransaction()");
		Neo4jTransactionObject txObject = new Neo4jTransactionObject();
		Transaction tx = (Transaction) TransactionSynchronizationManager
				.getResource(this.neoService);
		txObject.setTx(tx);
		return txObject;
	}

	protected boolean isExistingTransaction(Object transaction) {
		log.info("Neo4jTransactionManager.isExistingTransaction({})", transaction);
		Neo4jTransactionObject txObject = (Neo4jTransactionObject) transaction;
		return (txObject.getTx() != null && txObject.isTransactionActive());
	}

	protected void doBegin(Object transaction, TransactionDefinition definition) {
		log.info("doBegin("+transaction+", "+definition);
		Neo4jTransactionObject txObject = (Neo4jTransactionObject) transaction;
		try {
			txObject.setTx(neoService.beginTx());
			txObject.setTransactionActive(true);
			TransactionSynchronizationManager.bindResource(neoService, txObject
					.getTx());
		} catch (Exception ex) {
			throw new CannotCreateTransactionException(
					"Could not create Neo4j transaction", ex);
		}

	}

	protected void doCommit(DefaultTransactionStatus status) {
		log.info("Neo4jTransactionManager.doCommit({})", status);
		Neo4jTransactionObject txObject = (Neo4jTransactionObject) status
				.getTransaction();
		Transaction tx = txObject.getTx();
		if (status.isDebug()) {
			logger.debug("Committing Neo4j transaction");
		}
		try {
			tx.success();
			tx.finish();
		} catch (Exception ex) {
			throw new TransactionSystemException(
					"Could not commit Neo4j transaction", ex);
		}
	}

	protected void doRollback(DefaultTransactionStatus status) {
		log.info("Neo4jTransactionManager.doRollback({})", status);
		Neo4jTransactionObject txObject = (Neo4jTransactionObject) status
				.getTransaction();
		Transaction tx = txObject.getTx();
		if (status.isDebug()) {
			logger.debug("Rolling back Neo4j transaction");
		}
		try {
			tx.failure();
			tx.finish();
		} catch (Exception ex) {
			throw new TransactionSystemException(
					"Could not roll back Neo4j transaction", ex);
		}
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		log.info("cleaning up neo4j tx for "+transaction);
		Neo4jTransactionObject txObject = (Neo4jTransactionObject) transaction;
		TransactionSynchronizationManager.unbindResource(this.neoService);
	}

	private static final class Neo4jTransactionObject {
		private boolean transactionActive = false;
		private Transaction tx;
		private boolean newConnectionHolder;

		public boolean isTransactionActive() {
			return transactionActive;
		}

		public void setTransactionActive(boolean transactionActive) {
			this.transactionActive = transactionActive;
		}

		public Transaction getTx() {
			return tx;
		}

		public void setTx(Transaction tx) {
			this.tx = tx;
		}
	}
}
