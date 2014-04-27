package com.github.ATGMemcached;

import atg.adapter.gsa.GSAItemData;
import atg.adapter.gsa.GSAItemDescriptor;
import atg.adapter.gsa.GSARepository;
import atg.adapter.gsa.externalcache.GSAExternalCacheAdapter;
import net.spy.memcached.MemcachedClient;

import java.util.List;

public class GSAMemcachedAdapter implements GSAExternalCacheAdapter {

    private GSAMemcachedManager gsaMemcachedManager;

    private GSARepository gsaRepository;

    private GSAItemDescriptor gsaItemDescriptor;

    public GSAMemcachedAdapter(GSARepository gsaRepository, GSAItemDescriptor gsaItemDescriptor, GSAMemcachedManager gsaMemcachedManager) {
        this.gsaMemcachedManager = gsaMemcachedManager;
        this.gsaRepository = gsaRepository;
        this.gsaItemDescriptor = gsaItemDescriptor;
    }

    public MemcachedClient getDataStore() {
        return getGsaMemcachedManager().createDataStore();
    }

    private String createRepositoryNamespace() {
        return getGsaItemDescriptor().getItemDescriptorName() + ":" + getGsaMemcachedManager().generateNamespacePrefix();
    }

    private void initializeRepositoryCache() {
        getGsaMemcachedManager().vlogDebug("Creating namespace for " + getGsaItemDescriptor().getItemDescriptorName());
        getDataStore().add(getGsaItemDescriptor().getItemDescriptorName(),
                getGsaMemcachedManager().getDefaultTimeout(),
                createRepositoryNamespace());
    }

    private String getRepositoryNamespace() {
        return (String)getDataStore().get(getGsaItemDescriptor().getItemDescriptorName());
    }

    private String createNamespaceForItem(String key) {
        String repositoryNamespace = getRepositoryNamespace();
        if (repositoryNamespace == null) {
            getGsaMemcachedManager().vlogDebug("Namespace not found for " + getGsaItemDescriptor().getItemDescriptorName());
            initializeRepositoryCache();
        }
        return getRepositoryNamespace() + ":" + key;
    }

    @Override
    public GSAItemData getItemData(String s) {
        return (GSAItemData)getDataStore().get(createNamespaceForItem(s));
    }

    @Override
    public List<GSAItemData> putItemData(String s, GSAItemData gsaItemData) {
        getDataStore().add(createNamespaceForItem(s),
                getGsaMemcachedManager().getDefaultTimeout(),
                createRepositoryNamespace());
        return null;
    }

    @Override
    public void removeItemData(String s) {
         putItemData(s, null);
    }

    @Override
    public void invalidate() {
        initializeRepositoryCache();
    }

    /**
     * Not Used
     */
    @Override
    public int getEntryCount() {
        return 0;
    }

    /**
     * Not Used
     */
    @Override
    public String dump() {
        return null;
    }

    /**
     * Not Used
     */
    @Override
    public void resetStatistics() {

    }

    /**
     * Not Used
     */
    @Override
    public void shutdown() {

    }

    public GSAMemcachedManager getGsaMemcachedManager() {
        return gsaMemcachedManager;
    }

    public void setGsaMemcachedManager(GSAMemcachedManager gsaMemcachedManager) {
        this.gsaMemcachedManager = gsaMemcachedManager;
    }

    public GSARepository getGsaRepository() {
        return gsaRepository;
    }

    public void setGsaRepository(GSARepository gsaRepository) {
        this.gsaRepository = gsaRepository;
    }

    public GSAItemDescriptor getGsaItemDescriptor() {
        return gsaItemDescriptor;
    }

    public void setGsaItemDescriptor(GSAItemDescriptor gsaItemDescriptor) {
        this.gsaItemDescriptor = gsaItemDescriptor;
    }

}
