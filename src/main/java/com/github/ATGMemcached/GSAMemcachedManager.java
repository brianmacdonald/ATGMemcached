package com.github.ATGMemcached;

import atg.adapter.gsa.GSAItemDescriptor;
import atg.adapter.gsa.GSARepository;
import atg.adapter.gsa.externalcache.GSAExternalCacheAdapter;
import atg.adapter.gsa.externalcache.GSAExternalCacheManager;
import atg.nucleus.GenericService;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class GSAMemcachedManager extends GenericService implements GSAExternalCacheManager {
    private MemcachedClient client;
    private int defaultTimeout;
    private String addresses;

    @Override
    public GSAExternalCacheAdapter getExternalCacheAdapter(GSARepository gsaRepository, GSAItemDescriptor gsaItemDescriptor) {
        return new GSAMemcachedAdapter(gsaRepository, gsaItemDescriptor, this);
    }

    public MemcachedClient createDataStore() {
        MemcachedClient memcachedClient = null;
        try {
            List<InetSocketAddress> addresses = AddrUtil.getAddresses(getAddresses());
            memcachedClient = new MemcachedClient(addresses);
        } catch (IOException e) {
            vlogError("Could not create MemcachedClient " + e.getMessage());
        }
        return memcachedClient;
    }

    public String generateNamespacePrefix() {
        return Long.toString(System.currentTimeMillis());
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout){
        this.defaultTimeout = defaultTimeout;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }
}
