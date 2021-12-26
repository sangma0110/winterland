package com.jaess.winterland;

//public class UserAccount {
//    private String idToken;
//    private String email;
//    private String password;
//    /*
//        private String gender;
//        private String height;
//        private String weight;
//    */
//    private String name;
//    private String location;
//
//
//
//
//    private String imageName;
//    private String imageEmail;
//    private String imageDescription;
//
//    public UserAccount() { }
//
//    public String getIdToken() { return idToken; }
//    public void setIdToken(String idToken) {this.idToken = idToken;}
//
//    public String getEmail() {return email; }
//    public void setEmailId(String email) {this.email = email;}
//
//    public String getPassword() {return password;}
//    public void setPassword(String password) { this.password = password; }
//
//
//    /*
//        public String getGender() {return gender;}
//        public void setGender(String gender) { this.gender = gender; }
//
//        public String getHeight() {return height;}
//        public void setHeight(String height) { this.height = height; }
//
//        public String getweight() {return weight;}
//        public void setWeight(String weight) { this.weight = weight; }
//
//
//    */
//    public String getName() {return name;}
//    public void setName(String name) { this.name = name; }
//
//    public String getLocation() {return location;}
//    public void setLocation(String location) { this.name = location; }
//
//
//
//    public String getImageName() {return imageName;}
//    public void setImageName(String imageName) { this.imageName = imageName; }
//
//    public String getImageEmail() {return imageEmail;}
//    public void setImageEmail(String imageEmail) { this.imageEmail = imageEmail; }
//
//    public String getImageDescription() {return imageDescription;}
//    public void setImageDescription(String imageDescription) { this.location = imageDescription; }
//
//
//
//
//
//
//}
public class UserAccount {
    private String idToken;
    private String email;
    private String password;
    private String name;
    private String location;
    private String tokens;

    private String imageName;
    private String imageEmail;
    private String imageDescription;

    public UserAccount() { }

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) {this.idToken = idToken;}

    public String getEmail() {return email; }
    public void setEmailId(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password; }

    public String getName() {return name;}
    public void setName(String name) { this.name = name; }

    public String getLocation() {return location;}
    public void setLocation(String location) { this.location = location; }

    public String getTokens() { return tokens; }
    public void setTokens(String num) {this.tokens = num;}

    public String getImageName() {return imageName;}
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getImageEmail() {return imageEmail;}
    public void setImageEmail(String imageEmail) { this.imageEmail = imageEmail; }

    public String getImageDescription() {return imageDescription;}
    public void setImageDescription(String imageDescription) { this.location = imageDescription; } //재신아 이거 뭐임? 왜 this.location을 여기에썻어?
}

