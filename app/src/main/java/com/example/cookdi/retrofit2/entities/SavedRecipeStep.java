package com.example.cookdi.retrofit2.entities;

public class SavedRecipeStep extends RecipeStep {
        private byte[] imgStep;

        public void setImgStep(byte[] img){
             imgStep = img;
        }

        public byte[] getImgStep(){
             return imgStep;
        }
}
