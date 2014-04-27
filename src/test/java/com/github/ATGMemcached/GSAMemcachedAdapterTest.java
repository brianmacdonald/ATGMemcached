package com.github.ATGMemcached;

import atg.adapter.gsa.GSAItemData;
import atg.adapter.gsa.GSAItemDescriptor;
import atg.adapter.gsa.GSARepository;
import net.spy.memcached.MemcachedClient;
import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class GSAMemcachedAdapterTest {

    public GSAMemcachedManager gsaMemcachedManager;

    public GSARepository gsaRepository;

    public GSAItemDescriptor gsaItemDescriptor;

    public MemcachedClient memcachedClient;

    final public String itemDescriptorName = "TestItemDescriptor";

    final public int defaultTimeout = 3600;

    final public String namespacePrefix = "12345678901";

    @Before
    public void setUp() throws Exception {
        this.gsaMemcachedManager = mock(GSAMemcachedManager.class);
        when(this.gsaMemcachedManager.generateNamespacePrefix()).thenReturn(namespacePrefix);
        when(this.gsaMemcachedManager.getDefaultTimeout()).thenReturn(defaultTimeout);
        this.gsaRepository = mock(GSARepository.class);
        this.gsaItemDescriptor = mock(GSAItemDescriptor.class);
        when(this.gsaItemDescriptor.getItemDescriptorName()).thenReturn(itemDescriptorName);
        this.memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get(itemDescriptorName)).thenReturn(itemDescriptorName + ":" + namespacePrefix);
    }

    @Test
    public void testGetItemData() throws Exception {
        final String itemKey = "testGetOne";
        when(memcachedClient.get(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (!itemDescriptorName.equals(args[0])) {
                    Assert.assertEquals(itemDescriptorName + ":" + namespacePrefix + ":" + itemKey, args[0]);
                    return mock(GSAItemData.class);
                }
                return itemDescriptorName + ":" + namespacePrefix;
            }
        });
        when(this.gsaMemcachedManager.createDataStore()).thenReturn(memcachedClient);
        GSAMemcachedAdapter gsaMemcachedAdapter = new GSAMemcachedAdapter(this.gsaRepository, this.gsaItemDescriptor, this.gsaMemcachedManager);
        gsaMemcachedAdapter.getItemData(itemKey);
    }

    @Test
    public void testPutItemData() throws Exception {
        GSAItemData gsaItemData = mock(GSAItemData.class);
        final String itemKey = "testPutOne";
        when(memcachedClient.add(anyString(), anyInt(), anyObject())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Assert.assertEquals(itemDescriptorName + ":" + namespacePrefix + ":" + itemKey, args[0]);
                return null;
            }
        });
        when(this.gsaMemcachedManager.createDataStore()).thenReturn(memcachedClient);
        GSAMemcachedAdapter gsaMemcachedAdapter = new GSAMemcachedAdapter(this.gsaRepository, this.gsaItemDescriptor, this.gsaMemcachedManager);
        gsaMemcachedAdapter.putItemData(itemKey, gsaItemData);
    }

}
