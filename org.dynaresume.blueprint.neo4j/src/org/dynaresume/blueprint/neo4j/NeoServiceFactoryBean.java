package org.dynaresume.blueprint.neo4j;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class NeoServiceFactoryBean implements FactoryBean, InitializingBean {
	private String storeDir;
	private EmbeddedNeo ns;

	@Override
	public Object getObject() throws Exception {
		if (ns != null) {
			throw new RuntimeException("Neo4j server already created");
		}
		ns = new EmbeddedNeo(storeDir);
		ns.enableRemoteShell();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {

				if (ns != null) {
					ns.shutdown();
					System.out.println("NeoServiceFactoryBean.shutdown");
				}
			}
		});
		return ns;
	}

	@Override
	public Class getObjectType() {
		return NeoService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setStoreDir(String storeDir) {
		this.storeDir = storeDir;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (storeDir == null) {
			throw new IllegalArgumentException("storedir must be set");
		}
	}

}
