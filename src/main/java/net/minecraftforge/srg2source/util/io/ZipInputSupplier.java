/*
 * Srg2Source
 * Copyright (c) 2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.srg2source.util.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nullable;

import net.minecraftforge.srg2source.api.InputSupplier;
import net.minecraftforge.srg2source.util.Util;

public class ZipInputSupplier implements InputSupplier {
    public static ZipInputSupplier create(Path path, @Nullable Charset encoding) throws IOException {
        Map<String, byte[]> data = new HashMap<>();
        try (InputStream in = Files.newInputStream(path);
             ZipInputStream zin = new ZipInputStream(in)) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null)
                data.put(entry.getName(), Util.readStream(zin));
        }

        return new ZipInputSupplier(path.toString(), data, encoding);
    }

    private final String root;
    private final Map<String, byte[]> data;
    private final Charset encoding;

    private ZipInputSupplier(String root, Map<String, byte[]> data, @Nullable Charset encoding) {
        this.root = root;
        this.data = data;
        this.encoding = encoding;
    }

    @Override
    public void close() throws IOException {
        // useless here, since we read the zip ahead of time.
        // I wonder when this might actually be useful..
    }

    @Override
    public InputStream getInput(String relPath) {
        try {
            return new ByteArrayInputStream(data.get(relPath));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> gatherAll(List<String> endFilter) {
        LinkedList<String> out = new LinkedList<String>();

        for (String key : data.keySet())
            if (endFilter.stream().anyMatch(key::endsWith))
                out.add(key);

        return out;
    }

    @Override
    public String getRoot(String resource) {
        return root;
    }

    @Override
    public Charset getEncoding(String resource) {
        return encoding;
    }
}
