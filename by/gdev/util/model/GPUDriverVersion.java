package by.gdev.util.model;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/model/GPUDriverVersion.class */
public enum GPUDriverVersion {
    CUDA_V10_2("10.2"),
    CUDA_V10_1("10.1"),
    CUDA_V_10("10"),
    ANY_AMD("ANY_AMD");
    
    String value;

    GPUDriverVersion(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }
}
