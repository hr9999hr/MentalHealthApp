//Creator: Wong Jing Yi (101350)
//Tester: Tan Boon Sang (101173)

package modules;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// This class is responsible for loading learning modules from LearningModule.txt file
// It reads the file line by line, extracts module details, and creates LearningModule objects

public class LearningModuleLoader {
    public static List<LearningModule> getModulesFromFile(String filename) {
        List<LearningModule> modules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String title = null, summary = null, visualCue = null;
            String image = null, audio = null, video = null;
            List<String> contentLines = new ArrayList<>();

            boolean inContentSection = false;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                if (line.startsWith("Title:")) {
                    title = line.substring(6).trim();
                    inContentSection = false;
                } else if (line.startsWith("Description:")) {
                    summary = line.substring(12).trim();
                    inContentSection = false;
                } else if (line.startsWith("Content:")) {
                    inContentSection = true;
                    // Don't add the "Content:" header itself
                } else if (line.startsWith("Icon:")) {
                    visualCue = line.substring(5).trim();
                    inContentSection = false;
                } else if (line.startsWith("Image:")) {
                    image = line.substring(6).trim();
                    inContentSection = false;
                } else if (line.startsWith("Audio:")) {
                    audio = line.substring(6).trim();
                    inContentSection = false;
                } else if (line.startsWith("Video:")) {
                    video = line.substring(6).trim();
                    inContentSection = false;
                } else if (line.equals("---")) {
                    // End of module
                    if (title != null && summary != null) {
                        modules.add(new LearningModule(
                            title, summary, 
                            contentLines.toArray(new String[0]),  // Pass content lines
                            visualCue, image, audio, video
                        ));
                    }
                    // Reset variables
                    title = summary = visualCue = image = audio = video = null;
                    contentLines = new ArrayList<>();
                    inContentSection = false;
                } else if (inContentSection) {
                    // Preserve all content lines exactly as they appear
                    contentLines.add(line);
                }
            }
            
            // Add last module if exists
            if (title != null && summary != null) {
                modules.add(new LearningModule(
                    title, summary, contentLines.toArray(new String[0]),
                    visualCue, image, audio, video
                ));
            }
        } catch (IOException e) {
            Logger.getLogger(LearningModuleLoader.class.getName()).log(Level.SEVERE, null, e);
        }
        return modules;
    }
}