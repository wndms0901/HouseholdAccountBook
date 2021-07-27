const path = require('path');
const webpack = require('webpack');

function resolveSrc(_path) {
  return path.join(__dirname, _path);
}

module.exports = {
  outputDir: "../src/main/resources/static", // 빌드경로
  indexPath: "../static/index.html",
  devServer: {
    //https: true,
    //proxy: "http://localhost:8081"
  },
  lintOnSave: false,
  configureWebpack: {
    // Set up all the aliases we use in our app.
    resolve: {
      alias: {
        src: resolveSrc('src'),
        //  'chart.js': 'chart.js/dist/Chart.js'
      }
    },
    plugins: [
      // new webpack.optimize.LimitChunkCountPlugin({
      //   maxChunks: 6
      //    })
    ]
  },
  pwa: {
    name: 'Vue Light Bootstrap Dashboard',
    themeColor: '#344675',
    msTileColor: '#344675',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: '#344675'
  },
  css: {
    // Enable CSS source maps.
    sourceMap: process.env.NODE_ENV !== 'production'
  },
  //chainWebpack(config) {
  ///config.output.filename("js/[name].js");
  //빌드 시 빌드되어 나오는 js파일을 js폴더 아래로 묶어 빌드한다
  // const svgRule = config.module.rule("svg");
  // svgRule.uses.clear();
  // svgRule.use("vue-svg-loader").loader("vue-svg-loader");
  //},
};
