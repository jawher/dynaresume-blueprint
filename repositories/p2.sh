#!/bin/sh

REPO=mein
REPODIR=`cd $REPO; pwd`

/home/djo/java/ide/eclipse/eclipse-rcp-galileo-SR1/eclipse \
 -application org.eclipse.equinox.p2.metadata.generator.EclipseGenerator \
 -nosplash \
 -updateSite "$REPODIR" \
 -metadataRepository "file://$REPODIR" \
 -metadataRepositoryName "$REPO" \
 -artifactRepository "file://$REPODIR" \
 -artifactRepositoryName "$REPO" \
 -compress \
 -vmargs -Xmx512m \

