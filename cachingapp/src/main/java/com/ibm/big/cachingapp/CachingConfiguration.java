package com.ibm.big.cachingapp;

import java.net.URI;

import javax.cache.Cache;

import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteredStoreConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.common.Consistency;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CachingConfiguration
{

//	@Bean
//	public CacheManager cacheManager()
//	{
//		
//		CacheManagerBuilder<PersistentCacheManager> builder = CacheManagerBuilder.newCacheManagerBuilder()
//				.with(ClusteringServiceConfigurationBuilder.cluster(URI.create("terracotta://terracotta:9410/"))
//						.autoCreate()
//						.defaultServerResource("offheap-1"))
//				.withCache("books", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, 
//						ResourcePoolsBuilder.newResourcePoolsBuilder()
//						.with(ClusteredResourcePoolBuilder.clusteredDedicated("offheap-1", 32, MemoryUnit.MB)))
//							.add(ClusteredStoreConfigurationBuilder.withConsistency(Consistency.STRONG))
//						);
//		
//		PersistentCacheManager cacheManager = builder.build(true); 
//		return cacheManager;
//	}
	
}
