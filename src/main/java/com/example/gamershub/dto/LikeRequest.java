package com.example.gamershub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class LikeRequest  {
    private Long userid ; 
    private Long postid ; 
}
