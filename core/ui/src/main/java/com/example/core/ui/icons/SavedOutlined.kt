package com.example.core.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Outlined.Bookmarks: ImageVector
    get() {
        if (_bookmarks != null) {
            return _bookmarks!!
        }
        _bookmarks = materialIcon(name = "Outlined.Bookmarks") {
            materialPath {
                moveTo(15.0f, 7.0f)
                verticalLineToRelative(12.97f)
                lineToRelative(-4.21f, -1.81f)
                lineToRelative(-0.79f, -0.34f)
                lineToRelative(-0.79f, 0.34f)
                lineTo(5.0f, 19.97f)
                lineTo(5.0f, 7.0f)
                horizontalLineToRelative(10.0f)
                moveToRelative(4.0f, -6.0f)
                lineTo(8.99f, 1.0f)
                curveTo(7.89f, 1.0f, 7.0f, 1.9f, 7.0f, 3.0f)
                horizontalLineToRelative(10.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, 0.9f, 2.0f, 2.0f)
                verticalLineToRelative(13.0f)
                lineToRelative(2.0f, 1.0f)
                lineTo(21.0f, 3.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
                moveTo(15.0f, 5.0f)
                lineTo(5.0f, 5.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                verticalLineToRelative(16.0f)
                lineToRelative(7.0f, -3.0f)
                lineToRelative(7.0f, 3.0f)
                lineTo(17.0f, 7.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
            }
        }
        return _bookmarks!!
    }

private var _bookmarks: ImageVector? = null