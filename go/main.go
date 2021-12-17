package main

import (
	"github.com/gin-gonic/gin"
	"io"
	"os"
)

const dataDir = "../data/"

func main() {
	gin.SetMode(gin.ReleaseMode)
	server := gin.New()

	server.GET("/file/:file", func(ctx *gin.Context) {
		ctx.File(dataDir + ctx.Param("file"))
	})

	server.POST("/file", func(ctx *gin.Context) {
		file, _ := os.Create(dataDir + "")
		defer file.Close()

		_, _ = io.Copy(file, ctx.Request.Body)
	})

	server.GET("/static", func(ctx *gin.Context) {
		ctx.String(200, "This is a static text!")
	})

	_ = server.Run("0.0.0.0:5000")
}
