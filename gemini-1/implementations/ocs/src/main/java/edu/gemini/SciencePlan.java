package edu.gemini;

import edu.gemini.model.AbstractSciencePlan;
import java.util.Date;

public class SciencePlan extends AbstractSciencePlan {

    // === ENUMS ===
    public enum FileType { RAW, JPEG, PNG }
    public enum FileQuality { FINE, NORMAL, BASIC }
    public enum ColorType { BW_MODE, COLOR_MODE }

    // === FIELDS ===
    private String name;
    private int planNo;
    private String creator;
    private double funding;
    private String objective;
    private Date startDate;
    private Date endDate;
    private String telescope; 
    private String target;

    // Data processing attributes
    private FileType fileType;
    private FileQuality fileQuality;
    private ColorType colorType;

    private Double contrast;
    private Double brightness;
    private Double saturation;
    private Double highlights;
    private Double exposure;
    private Double shadows;
    private Double whites;
    private Double blacks;
    private Double luminance;
    private Double hue;

    private STATUS status;

    // === DEFAULT CONSTRUCTOR ===
    public SciencePlan() {}

    // === FULL CONSTRUCTOR (FIXED to use enums) ===
    public SciencePlan(
            int planNo, String name, String creator, double funding, String objective,
            Date startDate, Date endDate, String telescope, String target,
            FileType fileType, FileQuality fileQuality, ColorType colorType,
            Double contrast, Double brightness, Double saturation, Double highlights,
            Double exposure, Double shadows, Double whites, Double blacks,
            Double luminance, Double hue, STATUS status
    ) {
        this.planNo = planNo;
        this.name = name;
        this.creator = creator;
        this.funding = funding;
        this.objective = objective;
        this.startDate = startDate;
        this.endDate = endDate;
        this.telescope = telescope;
        this.target = target;

        this.fileType = fileType;
        this.fileQuality = fileQuality;
        this.colorType = colorType;

        this.contrast = contrast;
        this.brightness = brightness;
        this.saturation = saturation;
        this.highlights = highlights;
        this.exposure = exposure;
        this.shadows = shadows;
        this.whites = whites;
        this.blacks = blacks;
        this.luminance = luminance;
        this.hue = hue;

        this.status = status;
    }

    // === GETTERS / SETTERS ===
    public int getPlanNo() { return planNo; }
    public void setPlanNo(int planNo) { this.planNo = planNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public double getFunding() { return funding; }
    public void setFunding(double funding) { this.funding = funding; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getTelescope() { return telescope; }
    public void setTelescope(String telescope) { this.telescope = telescope; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    // === PROPER ENUM GETTERS/SETTERS ===
    public FileType getFileType() { return fileType; }
    public void setFileType(FileType fileType) { this.fileType = fileType; }

    public FileQuality getFileQuality() { return fileQuality; }
    public void setFileQuality(FileQuality fileQuality) { this.fileQuality = fileQuality; }

    public ColorType getColorType() { return colorType; }
    public void setColorType(ColorType colorType) { this.colorType = colorType; }

    public Double getContrast() { return contrast; }
    public void setContrast(Double contrast) { this.contrast = contrast; }

    public Double getBrightness() { return brightness; }
    public void setBrightness(Double brightness) { this.brightness = brightness; }

    public Double getSaturation() { return saturation; }
    public void setSaturation(Double saturation) { this.saturation = saturation; }

    public Double getHighlights() { return highlights; }
    public void setHighlights(Double highlights) { this.highlights = highlights; }

    public Double getExposure() { return exposure; }
    public void setExposure(Double exposure) { this.exposure = exposure; }

    public Double getShadows() { return shadows; }
    public void setShadows(Double shadows) { this.shadows = shadows; }

    public Double getWhites() { return whites; }
    public void setWhites(Double whites) { this.whites = whites; }

    public Double getBlacks() { return blacks; }
    public void setBlacks(Double blacks) { this.blacks = blacks; }

    public Double getLuminance() { return luminance; }
    public void setLuminance(Double luminance) { this.luminance = luminance; }

    public Double getHue() { return hue; }
    public void setHue(Double hue) { this.hue = hue; }

    public STATUS getStatus() { return status; }
    public void setStatus(STATUS status) { this.status = status; }

    @Override
    public String toString() {
        return "SciencePlan{" +
                "planNo=" + planNo +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", funding=" + funding +
                ", objective='" + objective + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", telescope='" + telescope + '\'' +
                ", target='" + target + '\'' +
                ", fileType=" + fileType +
                ", fileQuality=" + fileQuality +
                ", colorType=" + colorType +
                ", status=" + status +
                '}';
    }
}