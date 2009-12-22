#!/bin/sh

REPO=mein
REPODIR=`cd $REPO; pwd`

/home/djo/java/ide/eclipse/eclipse-rcp-galileo-SR1/eclipse \
 -application org.eclipse.equinox.p2.metadata.generator.EclipseGenerator \
 -nosplash \
 -updateSite "$REPODIR" \
 -site "file://$REPODIR/site.xml" \
 -metadataRepository "file://$REPODIR" \
 -metadataRepositoryName "$REPO" \
 -artifactRepository "file://$REPODIR" \
 -artifactRepositoryName "$REPO" \
 -noDefaultIUs \
 -vmargs -Xmx512m
