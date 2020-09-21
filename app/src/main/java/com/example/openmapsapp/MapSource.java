package com.example.openmapsapp;

import org.osmdroid.tileprovider.tilesource.XYTileSource;

public class MapSource extends XYTileSource {

    public MapSource(String aName, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding, String[] aBaseUrl) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding, aBaseUrl);
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return super.getTileURLString(pMapTileIndex);
    }
}
