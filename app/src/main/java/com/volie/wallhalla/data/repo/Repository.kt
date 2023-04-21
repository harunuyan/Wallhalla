package com.volie.wallhalla.data.repo

import com.volie.wallhalla.data.service.WallpaperService
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: WallpaperService,
)