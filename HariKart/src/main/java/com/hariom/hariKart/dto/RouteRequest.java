package com.hariom.hariKart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RouteRequest(@NotNull @Min(1) Integer route) {}