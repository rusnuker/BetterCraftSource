// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ResourceLeakDetectorFactory;
import java.util.ArrayList;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import java.util.List;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.AbstractReferenceCounted;

public abstract class AbstractDnsMessage extends AbstractReferenceCounted implements DnsMessage
{
    private static final ResourceLeakDetector<DnsMessage> leakDetector;
    private static final int SECTION_QUESTION;
    private static final int SECTION_COUNT = 4;
    private final ResourceLeakTracker<DnsMessage> leak;
    private short id;
    private DnsOpCode opCode;
    private boolean recursionDesired;
    private byte z;
    private Object questions;
    private Object answers;
    private Object authorities;
    private Object additionals;
    
    protected AbstractDnsMessage(final int id) {
        this(id, DnsOpCode.QUERY);
    }
    
    protected AbstractDnsMessage(final int id, final DnsOpCode opCode) {
        this.leak = (ResourceLeakTracker<DnsMessage>)AbstractDnsMessage.leakDetector.track(this);
        this.setId(id);
        this.setOpCode(opCode);
    }
    
    @Override
    public int id() {
        return this.id & 0xFFFF;
    }
    
    @Override
    public DnsMessage setId(final int id) {
        this.id = (short)id;
        return this;
    }
    
    @Override
    public DnsOpCode opCode() {
        return this.opCode;
    }
    
    @Override
    public DnsMessage setOpCode(final DnsOpCode opCode) {
        this.opCode = ObjectUtil.checkNotNull(opCode, "opCode");
        return this;
    }
    
    @Override
    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }
    
    @Override
    public DnsMessage setRecursionDesired(final boolean recursionDesired) {
        this.recursionDesired = recursionDesired;
        return this;
    }
    
    @Override
    public int z() {
        return this.z;
    }
    
    @Override
    public DnsMessage setZ(final int z) {
        this.z = (byte)(z & 0x7);
        return this;
    }
    
    @Override
    public int count(final DnsSection section) {
        return this.count(sectionOrdinal(section));
    }
    
    private int count(final int section) {
        final Object records = this.sectionAt(section);
        if (records == null) {
            return 0;
        }
        if (records instanceof DnsRecord) {
            return 1;
        }
        final List<DnsRecord> recordList = (List<DnsRecord>)records;
        return recordList.size();
    }
    
    @Override
    public int count() {
        int count = 0;
        for (int i = 0; i < 4; ++i) {
            count += this.count(i);
        }
        return count;
    }
    
    @Override
    public <T extends DnsRecord> T recordAt(final DnsSection section) {
        return this.recordAt(sectionOrdinal(section));
    }
    
    private <T extends DnsRecord> T recordAt(final int section) {
        final Object records = this.sectionAt(section);
        if (records == null) {
            return null;
        }
        if (records instanceof DnsRecord) {
            return castRecord(records);
        }
        final List<DnsRecord> recordList = (List<DnsRecord>)records;
        if (recordList.isEmpty()) {
            return null;
        }
        return castRecord(recordList.get(0));
    }
    
    @Override
    public <T extends DnsRecord> T recordAt(final DnsSection section, final int index) {
        return this.recordAt(sectionOrdinal(section), index);
    }
    
    private <T extends DnsRecord> T recordAt(final int section, final int index) {
        final Object records = this.sectionAt(section);
        if (records == null) {
            throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
        }
        if (!(records instanceof DnsRecord)) {
            final List<DnsRecord> recordList = (List<DnsRecord>)records;
            return castRecord(recordList.get(index));
        }
        if (index == 0) {
            return castRecord(records);
        }
        throw new IndexOutOfBoundsException("index: " + index + "' (expected: 0)");
    }
    
    @Override
    public DnsMessage setRecord(final DnsSection section, final DnsRecord record) {
        this.setRecord(sectionOrdinal(section), record);
        return this;
    }
    
    private void setRecord(final int section, final DnsRecord record) {
        this.clear(section);
        this.setSection(section, checkQuestion(section, record));
    }
    
    @Override
    public <T extends DnsRecord> T setRecord(final DnsSection section, final int index, final DnsRecord record) {
        return this.setRecord(sectionOrdinal(section), index, record);
    }
    
    private <T extends DnsRecord> T setRecord(final int section, final int index, final DnsRecord record) {
        checkQuestion(section, record);
        final Object records = this.sectionAt(section);
        if (records == null) {
            throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
        }
        if (!(records instanceof DnsRecord)) {
            final List<DnsRecord> recordList = (List<DnsRecord>)records;
            return castRecord(recordList.set(index, record));
        }
        if (index == 0) {
            this.setSection(section, record);
            return castRecord(records);
        }
        throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
    }
    
    @Override
    public DnsMessage addRecord(final DnsSection section, final DnsRecord record) {
        this.addRecord(sectionOrdinal(section), record);
        return this;
    }
    
    private void addRecord(final int section, final DnsRecord record) {
        checkQuestion(section, record);
        final Object records = this.sectionAt(section);
        if (records == null) {
            this.setSection(section, record);
            return;
        }
        if (records instanceof DnsRecord) {
            final List<DnsRecord> recordList = newRecordList();
            recordList.add(castRecord(records));
            recordList.add(record);
            this.setSection(section, recordList);
            return;
        }
        final List<DnsRecord> recordList = (List<DnsRecord>)records;
        recordList.add(record);
    }
    
    @Override
    public DnsMessage addRecord(final DnsSection section, final int index, final DnsRecord record) {
        this.addRecord(sectionOrdinal(section), index, record);
        return this;
    }
    
    private void addRecord(final int section, final int index, final DnsRecord record) {
        checkQuestion(section, record);
        final Object records = this.sectionAt(section);
        if (records == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
            }
            this.setSection(section, record);
        }
        else {
            if (records instanceof DnsRecord) {
                List<DnsRecord> recordList;
                if (index == 0) {
                    recordList = newRecordList();
                    recordList.add(record);
                    recordList.add(castRecord(records));
                }
                else {
                    if (index != 1) {
                        throw new IndexOutOfBoundsException("index: " + index + " (expected: 0 or 1)");
                    }
                    recordList = newRecordList();
                    recordList.add(castRecord(records));
                    recordList.add(record);
                }
                this.setSection(section, recordList);
                return;
            }
            List<DnsRecord> recordList = (List<DnsRecord>)records;
            recordList.add(index, record);
        }
    }
    
    @Override
    public <T extends DnsRecord> T removeRecord(final DnsSection section, final int index) {
        return this.removeRecord(sectionOrdinal(section), index);
    }
    
    private <T extends DnsRecord> T removeRecord(final int section, final int index) {
        final Object records = this.sectionAt(section);
        if (records == null) {
            throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
        }
        if (!(records instanceof DnsRecord)) {
            final List<DnsRecord> recordList = (List<DnsRecord>)records;
            return castRecord(recordList.remove(index));
        }
        if (index != 0) {
            throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
        }
        final T record = castRecord(records);
        this.setSection(section, null);
        return record;
    }
    
    @Override
    public DnsMessage clear(final DnsSection section) {
        this.clear(sectionOrdinal(section));
        return this;
    }
    
    @Override
    public DnsMessage clear() {
        for (int i = 0; i < 4; ++i) {
            this.clear(i);
        }
        return this;
    }
    
    private void clear(final int section) {
        final Object recordOrList = this.sectionAt(section);
        this.setSection(section, null);
        if (recordOrList instanceof ReferenceCounted) {
            ((ReferenceCounted)recordOrList).release();
        }
        else if (recordOrList instanceof List) {
            final List<DnsRecord> list = (List<DnsRecord>)recordOrList;
            if (!list.isEmpty()) {
                for (final Object r : list) {
                    ReferenceCountUtil.release(r);
                }
            }
        }
    }
    
    @Override
    public DnsMessage touch() {
        return (DnsMessage)super.touch();
    }
    
    @Override
    public DnsMessage touch(final Object hint) {
        if (this.leak != null) {
            this.leak.record(hint);
        }
        return this;
    }
    
    @Override
    public DnsMessage retain() {
        return (DnsMessage)super.retain();
    }
    
    @Override
    public DnsMessage retain(final int increment) {
        return (DnsMessage)super.retain(increment);
    }
    
    @Override
    protected void deallocate() {
        this.clear();
        final ResourceLeakTracker<DnsMessage> leak = this.leak;
        if (leak != null) {
            final boolean closed = leak.close(this);
            assert closed;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DnsMessage)) {
            return false;
        }
        final DnsMessage that = (DnsMessage)obj;
        if (this.id() != that.id()) {
            return false;
        }
        if (this instanceof DnsQuery) {
            if (!(that instanceof DnsQuery)) {
                return false;
            }
        }
        else if (that instanceof DnsQuery) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.id() * 31 + ((this instanceof DnsQuery) ? 0 : 1);
    }
    
    private Object sectionAt(final int section) {
        switch (section) {
            case 0: {
                return this.questions;
            }
            case 1: {
                return this.answers;
            }
            case 2: {
                return this.authorities;
            }
            case 3: {
                return this.additionals;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private void setSection(final int section, final Object value) {
        switch (section) {
            case 0: {
                this.questions = value;
                return;
            }
            case 1: {
                this.answers = value;
                return;
            }
            case 2: {
                this.authorities = value;
                return;
            }
            case 3: {
                this.additionals = value;
                return;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private static int sectionOrdinal(final DnsSection section) {
        return ObjectUtil.checkNotNull(section, "section").ordinal();
    }
    
    private static DnsRecord checkQuestion(final int section, final DnsRecord record) {
        if (section == AbstractDnsMessage.SECTION_QUESTION && !(ObjectUtil.checkNotNull(record, "record") instanceof DnsQuestion)) {
            throw new IllegalArgumentException("record: " + record + " (expected: " + StringUtil.simpleClassName(DnsQuestion.class) + ')');
        }
        return record;
    }
    
    private static <T extends DnsRecord> T castRecord(final Object record) {
        return (T)record;
    }
    
    private static ArrayList<DnsRecord> newRecordList() {
        return new ArrayList<DnsRecord>(2);
    }
    
    static {
        leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(DnsMessage.class);
        SECTION_QUESTION = DnsSection.QUESTION.ordinal();
    }
}
