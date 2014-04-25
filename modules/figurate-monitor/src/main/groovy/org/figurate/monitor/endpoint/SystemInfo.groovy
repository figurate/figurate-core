package org.figurate.monitor.endpoint

import groovy.json.JsonBuilder
import org.amdatu.web.rest.doc.Description
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import java.lang.management.ManagementFactory

/**
 * Created by fortuna on 23/04/14.
 */
@Component(immediate = true)
@Service(value = SystemInfo)
@Path("system")
@Description("Provides information about operating system resources")
class SystemInfo {

    @GET
    @Path('filesystem')
    @Produces("application/json")
    String getFilesystemInfo() {
        def builder = new JsonBuilder()
        builder(filesystems: File.listRoots().collectEntries { filesystem ->
            [ "$filesystem": ['totalSpace', 'usableSpace', 'freeSpace'].collectEntries {
                        ["$it": [bytes: filesystem[it], humanReadable: humanReadableByteCount(filesystem[it])]]
                    }]})
        builder.toPrettyString()
    }

    @GET
    @Path('memory')
    @Produces("application/json")
    String getMemoryInfo() {
        def heapMemoryUsage = ManagementFactory.memoryMXBean.heapMemoryUsage
        def nonHeapMemoryUsage = ManagementFactory.memoryMXBean.nonHeapMemoryUsage

        def builder = new JsonBuilder()
        builder(memory: [
                heap: [
                        ['init', 'max', 'committed', 'used'].collectEntries {
                            ["$it": [bytes: heapMemoryUsage[it], humanReadable: humanReadableByteCount(heapMemoryUsage[it])]]
                        }
                ],
                nonHeap: [
                        ['init', 'max', 'committed', 'used'].collectEntries {
                            ["$it": [bytes: nonHeapMemoryUsage[it], humanReadable: humanReadableByteCount(nonHeapMemoryUsage[it])]]
                        }
                ]])
        builder.toPrettyString()
    }

    @GET
    @Path('cpu')
    @Produces("application/json")
    String getCPUInfo() {
        def builder = new JsonBuilder()
        builder(cpus: [
                processors: ManagementFactory.operatingSystemMXBean.availableProcessors,
                loadAverage: ManagementFactory.operatingSystemMXBean.systemLoadAverage])
        builder.toPrettyString()
    }

    private String humanReadableByteCount(long bytes, boolean si = false) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "${(si ? 'kMGTPE' : 'KMGTPE').charAt(exp-1)}${(si ? '' : 'i')}";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
