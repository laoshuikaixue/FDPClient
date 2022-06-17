/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.FileUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts {

    @FontDetails(fontName = "Light", fontSize = 18, fileName = "regular.ttf")
    public static GameFontRenderer font18;

    @FontDetails(fontName = "Medium", fontSize = 32, fileName = "regular.ttf")
    public static GameFontRenderer font32;

    @FontDetails(fontName = "Medium", fontSize = 35, fileName = "regular.ttf")
    public static GameFontRenderer font35;

    @FontDetails(fontName = "Medium", fontSize = 40, fileName = "regular.ttf")
    public static GameFontRenderer font40;

    @FontDetails(fontName = "Medium", fontSize = 72, fileName = "regular.ttf")
    public static GameFontRenderer font72;

    @FontDetails(fontName = "SFTHIN Regular", fontSize = 17, fileName = "SFTHIN.ttf")
    public static GameFontRenderer fontSFTHIN17;

    @FontDetails(fontName = "SFTHIN Regular", fontSize = 18, fileName = "SFTHIN.ttf")
    public static GameFontRenderer fontSFTHIN18;

    @FontDetails(fontName = "SFTHIN Regular", fontSize = 19, fileName = "SFTHIN.ttf")
    public static GameFontRenderer fontSFTHIN19;

    @FontDetails(fontName = "SFTHIN Regular", fontSize = 20, fileName = "SFTHIN.ttf")
    public static GameFontRenderer fontSFTHIN20;

    @FontDetails(fontName = "Roboto Regular", fontSize = 40, fileName = "Roboto-Bold.ttf")
    public static GameFontRenderer fontRoboto40;

    @FontDetails(fontName = "Roboto Regular", fontSize = 30, fileName = "Roboto-Bold.ttf")
    public static GameFontRenderer fontRoboto30;

    @FontDetails(fontName = "Roboto Regular", fontSize = 50, fileName = "Roboto-Bold.ttf")
    public static GameFontRenderer fontRoboto50;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 14, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa14;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 18, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa18;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 24, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa24;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 30, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa30;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 35, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa35;

    @FontDetails(fontName = "Comfortaa Regular", fontSize = 40, fileName = "Comfortaa.ttf")
    public static GameFontRenderer fontComfortaa40;

    @FontDetails(fontName = "XiYuanGBT Regular", fontSize = 35, fileName = "GEETYPE-XiYuanGBT-Flash.ttf")
    public static GameFontRenderer fontXiYuanGBT35;

    @FontDetails(fontName = "Whitney-Book Regular", fontSize = 35, fileName = "Whitney-Book.ttf")
    public static GameFontRenderer fontWhitneyBook35;

    @FontDetails(fontName = "Whitney-Book Regular", fontSize = 40, fileName = "Whitney-Book.ttf")
    public static GameFontRenderer fontWhitneyBook40;

    @FontDetails(fontName = "Whitney-Light Regular", fontSize = 35, fileName = "Whitney-Light.ttf")
    public static GameFontRenderer fontWhitneyLight35;

    @FontDetails(fontName = "Whitney-Light Regular", fontSize = 40, fileName = "Whitney-Light.ttf")
    public static GameFontRenderer fontWhitneyLight40;

    @FontDetails(fontName = "Small", fontSize = 30, fileName = "GoogleSans.ttf")
    public static GameFontRenderer fontgoogle30;

    @FontDetails(fontName = "Small", fontSize = 35, fileName = "GoogleSans.ttf")
    public static GameFontRenderer fontgoogle35;

    @FontDetails(fontName = "SFUI Regular", fontSize = 35, fileName = "sfui.ttf")
    public static GameFontRenderer fontSFUI35;

    @FontDetails(fontName = "SFUI Regular", fontSize = 40, fileName = "sfui.ttf")
    public static GameFontRenderer fontSFUI40;

    @FontDetails(fontName = "Tahoma Regular", fontSize = 35, fileName = "tahoma.ttf")
    public static GameFontRenderer fonttahoma35;

    @FontDetails(fontName = "MiSansNormal Regular", fontSize = 30, fileName = "MiSans-Normal.ttf")
    public static GameFontRenderer fontMiSansNormal30;

    @FontDetails(fontName = "MiSansNormal Regular", fontSize = 32, fileName = "MiSans-Normal.ttf")
    public static GameFontRenderer fontMiSansNormal32;

    @FontDetails(fontName = "MiSansNormal Regular", fontSize = 35, fileName = "MiSans-Normal.ttf")
    public static GameFontRenderer fontMiSansNormal35;

    @FontDetails(fontName = "MiSansLight Regular", fontSize = 40, fileName = "MiSans-Normal.ttf")
    public static GameFontRenderer fontMiSansNormal40;

    @FontDetails(fontName = "MiSansLight Regular", fontSize = 35, fileName = "MiSans-Light.ttf")
    public static GameFontRenderer fontMiSansLight35;

    @FontDetails(fontName = "MiSansNormal Regular", fontSize = 40, fileName = "MiSans-Light.ttf")
    public static GameFontRenderer fontMiSansLight40;

    @FontDetails(fontName = "ICON", fontSize = 20, fileName = "stylesicons.ttf")
    public static GameFontRenderer fontstylesicon20;


//    @FontDetails(fontName = "Huge", fontSize = 60, fileName = "regular.ttf")
//    public static GameFontRenderer font60;

    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont = Minecraft.getMinecraft().fontRendererObj;

    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS = new ArrayList<>();

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.INSTANCE.logInfo("Loading Fonts.");

        downloadFonts();

        for(GameFontRenderer it : getCustomFonts()) {
            it.close();
        }

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.getFontsDir(), "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.INSTANCE.logInfo("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.fileManager.getFontsDir(), "fdpclient-fonts.zip");
            final File Comfortaa = new File(LiquidBounce.fileManager.getFontsDir(), "Comfortaa.ttf");
            final File GEETYPEXiYuanGBTFlash = new File(LiquidBounce.fileManager.getFontsDir(), "GEETYPE-XiYuanGBT-Flash.ttf");
            final File GoogleSans = new File(LiquidBounce.fileManager.getFontsDir(), "GoogleSans.ttf");
            final File MiSansLight = new File(LiquidBounce.fileManager.getFontsDir(), "MiSans-Light.ttf");
            final File MiSansNormal = new File(LiquidBounce.fileManager.getFontsDir(), "MiSans-Normal.ttf");
            final File regular = new File(LiquidBounce.fileManager.getFontsDir(), "regular.ttf");
            final File RobotoBold = new File(LiquidBounce.fileManager.getFontsDir(), "Roboto-Bold.ttf");
            final File SFTHIN = new File(LiquidBounce.fileManager.getFontsDir(), "SFTHIN.ttf");
            final File sfui = new File(LiquidBounce.fileManager.getFontsDir(), "sfui.ttf");
            final File stylesicons = new File(LiquidBounce.fileManager.getFontsDir(), "stylesicons.ttf");
            final File tahoma = new File(LiquidBounce.fileManager.getFontsDir(), "tahoma.ttf");
            final File WhitneyBook = new File(LiquidBounce.fileManager.getFontsDir(), "Whitney-Book.ttf");
            final File WhitneyLight = new File(LiquidBounce.fileManager.getFontsDir(), "Whitney-Light.ttf");

            if (!outputFile.exists() || !Comfortaa.exists() || !GEETYPEXiYuanGBTFlash.exists() || !GoogleSans.exists() || !MiSansLight.exists() ||
                    !MiSansNormal.exists() || !regular.exists() || !RobotoBold.exists() || !SFTHIN.exists() || !sfui.exists() || !stylesicons.exists() ||
                    !tahoma.exists() || !WhitneyBook.exists() || WhitneyLight.exists()
            ) {
                ClientUtils.INSTANCE.logInfo("Downloading fonts...");
                HttpUtils.download("https://pan.bilnn.com/api/v3/file/sourcejump/3nB4k7HD/mnu4dU14NfDz-PlcE4rKMDS2eHrJc97Oj_OtgbK6T9E*", outputFile);
                ClientUtils.INSTANCE.logInfo("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidBounce.fileManager.getFontsDir().getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if(!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSingleFont(String name, String resourcePath) throws IOException {
        File file=new File(LiquidBounce.fileManager.getFontsDir(), name);
        if(!file.exists())
            FileUtils.INSTANCE.unpackFile(file, resourcePath);
    }

    public static FontRenderer getFontRenderer(final String name, final int size) {
        if(name.equals("Minecraft")){
            return minecraftFont;
        }

        for (final FontRenderer fontRenderer : getFonts()) {
            if(fontRenderer instanceof GameFontRenderer){
                GameFontRenderer liquidFontRenderer=(GameFontRenderer) fontRenderer;
                final Font font = liquidFontRenderer.getDefaultFont().getFont();

                if(font.getName().equals(name) && font.getSize() == size)
                    return liquidFontRenderer;
            }
        }

        return minecraftFont;
    }

    public static Object[] getFontDetails(final FontRenderer fontRenderer) {
        if (fontRenderer instanceof GameFontRenderer) {
            final Font font = ((GameFontRenderer) fontRenderer).getDefaultFont().getFont();
            return new Object[] {font.getName(), font.getSize()};
        }

        return new Object[] {"Minecraft", -1};
    }

    public static List<FontRenderer> getFonts() {
        final List<FontRenderer> fonts = new ArrayList<>();

        for(final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if(fontObj instanceof FontRenderer) fonts.add((FontRenderer) fontObj);
            }catch(final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);

        return fonts;
    }

    public static List<GameFontRenderer> getCustomFonts() {
        final List<GameFontRenderer> fonts = new ArrayList<>();

        for(final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if(fontObj instanceof GameFontRenderer) fonts.add((GameFontRenderer) fontObj);
            }catch(final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.getFontsDir(), fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        }catch(final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }
}