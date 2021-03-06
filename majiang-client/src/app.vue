<template>
    <div id='app' class='app'>
        <!--<div class="card">-->
            <!--<span class="cardTitle">日分布折线图</span>-->
            <!--<div id="line" class='plot'></div>-->
        <!--</div>-->
        <div class="card">
            <span class="cardTitle">日分布动态条形图&nbsp;&nbsp;<a href="javascript:;" @click="playDynamicBar">播放</a></span>
            <div id="bar" class='plot'></div>
        </div>
        <div class="card">
            <span class="cardTitle">局分布散点图</span>
            <div id="scatter" class='plot'></div>
        </div>
        <div class="card">
            <span class="cardTitle">时分布热力图</span>
            <div id="heat" class='plot'></div>
        </div>
        <div v-for="(f, i) in friends" class="card">
            <span class="cardTitle">{{f + '的日分布面积图'}}</span>
            <div :id=getWaterFallName(i) class='plot'></div>
        </div>
        <br>
        <br>
        <br>
    </div>
</template>

<script>
    import util from './libs/util';
    import { fetchWaterFall, fetchScatter, fetchLine, fetchHeat, fetchDynamicBar } from './api/query';

    export default {
        components: {
        },
        data () {
            return {
                xAxis: {
                    line: false,
                    tickCount: util.isMobile() ? 10 : 20,
                    grid: {
                        line: {
                            style: {
                                stroke: 'black',
                                lineWidth: 1,
                                lineDash: [3, 4],
                                strokeOpacity: 0.2
                            }
                        }
                    }
                },
                yAxis: {
                    grid: {
                        line: {
                            style: {
                                stroke: 'black',
                                lineWidth: 1,
                                lineDash: [3, 4],
                                strokeOpacity: 0.2
                            }
                        }
                    }
                },
                friends: ['ZnS', '孤独侠客', '无止无境', 'kevin chen', '呐喊', '蜗牛爬爬', '123', '飞锅'],
                color: [
                    '#168416',
                    '#6422aa',
                    '#00b0f0',
                    '#2828ef',
                    '#ff0000',
                    '#4c4c4c',
                    '#b78080',
                    '#ff7f00'
                ],
                dynamicBarChart: null,
                dynamicBarData: [],
                dynamicBarInterval: null
            };
        },
        methods: {
            getWaterFallName (i) {
                return 'waterFall_' + i;
            },
            createWaterFall (containerName, waterFall) {
                let formatter = (v) => `${v}`;
                let annotations = [];
                let data = waterFall.data;
                data.reduce((v, d) => {
                    annotations.push({
                        type: 'text',
                        position: () => {
                            const y = v + d.y / 2;
                            return [d.x, y];
                        },
                        content: util.isMobile() ? '' : formatter(d.y),
                        style: {
                            fontSize: 10,
                            // stroke: '#666',
                            fill: '#fff',
                            lineWidth: 1,
                            textAlign: 'center',
                            verticalAlign: 'middle'
                        }
                    });
                    return v + d.y;
                }, 0);

                let waterfallPlot = new this.$G2Plot.Waterfall(containerName, {
                    data,
                    padding: 'auto',
                    appendPadding: [20, 0, 0, 0],
                    theme: 'default', // 'dark',
                    xField: 'x',
                    yField: 'y',
                    meta: {
                        x: {
                            alias: '日期'
                        },
                        y: {
                            alias: '盈亏',
                            formatter
                        }
                    },
                    total: {
                        label: '总计',
                        style: {
                            fill: 'rgba(0, 0, 0, 1)'
                        }
                    },
                    labelMode: 'absolute',
                    label: {
                        style: {
                            fontSize: 12
                        }
                    },
                    risingFill: '#ff0000',
                    fallingFill: '#168416',
                    xAxis: this.xAxis,
                    yAxis: this.yAxis,
                    legend: false,
                    annotations
                });
                waterfallPlot.render();
            },

            createScatter (containerName, scatter) {
                let friends = this.friends;
                let data = scatter;
                let size = util.isMobile() ? 30 : 50;
                let offsetX = util.isMobile() ? -40 : -50;
                let max = -99999;

                data.map(d => {
                    if (d.y > max) {
                        max = d.y;
                    }
                });
                this.$G2Plot.G2.registerShape('point', 'image', {
                    draw (cfg, container) {
                        cfg.points = this.parsePoints(cfg.points);
                        return container.addShape('image', {
                            attrs: {
                                x: cfg.points[0].x - (cfg.size / 2),
                                y: cfg.points[0].y - (cfg.size / 2),
                                width: cfg.size,
                                height: cfg.size,
                                img: './data/' + friends.indexOf(cfg.data.name) + '.png'
                            }
                        });
                    }
                });
                let scatterPlot = new this.$G2Plot.Scatter(containerName, {
                    appendPadding: 10,
                    data,
                    xField: 'x',
                    yField: 'y',
                    shape: 'image',
                    size: size,
                    xAxis: this.xAxis,
                    yAxis: this.yAxis,
                    label: {
                        offsetX: offsetX,
                        formatter: (item) => {
                            let label = item.x + ',' + item.y;
                            if (max === item.y) {
                                label = 'GOAT\n' + label;
                            }
                            return label;
                        },
                        style: {
                            fill: 'rgba(0,0,0,1)'
                        }
                    },
                    legend: false
                });
                scatterPlot.render();
            },

            createLine (containerName, scatter) {
                let data = scatter;
                let linePlot = new this.$G2Plot.Line(containerName, {
                    data,
                    xField: 'x',
                    yField: 'y',
                    seriesField: 'name',
                    legend: {
                        position: 'bottom'
                    },
                    color: this.color,
                    smooth: true,
                    connectNulls: true,
                    xAxis: this.xAxis,
                    yAxis: this.yAxis,
                    animation: {
                        appear: {
                            animation: 'wave-in',
                            duration: 15000
                        }
                    }
                });

                linePlot.render();
            },

            createArea (containerName, scatter, color) {
                let data = scatter;
                let areaPlot = new this.$G2Plot.Area(containerName, {
                    data,
                    xField: 'x',
                    yField: 'y',
                    xAxis: this.xAxis,
                    yAxis: this.yAxis,
                    legend: false,
                    connectNulls: true,
                    smooth: true,
                    color: color
                    // areaStyle: () => {
                    //     return {
                    //         fill: 'l(270) 0:#ffffff 0.5:#7ec2f3 1:#1890ff'
                    //     };
                    // }
                });

                areaPlot.render();
            },

            createHeat (containerName, heat) {
                let data = heat;
                let max = -99999;
                let min = 99999;
                let looseR = [];
                let looseG = [];
                let looseB = [];
                let winR = [];
                let winG = [];
                let winB = [];
                let looseColor = [
                    '#ADF3FF',
                    '#5ED3FF',
                    '#1890ff',
                    '#0050b3'
                ];
                let winColor = [
                    '#FACDC8',
                    '#FA8888',
                    '#FA4145',
                    '#ff0000'
                ];
                let ratio = [0.0, 0.2, 0.5, 1];

                looseColor.map(color => {
                    let r = parseInt(color.substr(1, 2), 16);
                    let g = parseInt(color.substr(3, 2), 16);
                    let b = parseInt(color.substr(5, 2), 16);
                    looseR.push(r);
                    looseG.push(g);
                    looseB.push(b);
                });

                winColor.map(color => {
                    let r = parseInt(color.substr(1, 2), 16);
                    let g = parseInt(color.substr(3, 2), 16);
                    let b = parseInt(color.substr(5, 2), 16);
                    winR.push(r);
                    winG.push(g);
                    winB.push(b);
                });

                data.map(d => {
                    if (d.v > max) {
                        max = d.v;
                    }
                    if (d.v < min) {
                        min = d.v;
                    }
                });

                let heatColor = (v, min, max) => {
                    let c;
                    let t;
                    let f;
                    let r, g, b;
                    if (v <= 0) {
                        t = v * 1.0 / min;
                        r = looseR;
                        g = looseG;
                        b = looseB;
                    } else {
                        t = v * 1.0 / max;
                        r = winR;
                        g = winG;
                        b = winB;
                    }
                    for (let i = 1; i < ratio.length; i++) {
                        let low = ratio[i - 1];
                        let high = ratio[i];
                        let lowR = r[i - 1];
                        let highR = r[i];
                        let lowG = g[i - 1];
                        let highG = g[i];
                        let lowB = b[i - 1];
                        let highB = b[i];
                        if (t <= ratio[i]) {
                            f = (t - low) / (high - low);
                            let red = Math.floor((highR - lowR) * f + lowR);
                            let green = Math.floor((highG - lowG) * f + lowG);
                            let blue = Math.floor((highB - lowB) * f + lowB);
                            c = 'rgba(' + red + ',' + green + ',' + blue + ', 1)';
                            break;
                        }
                    }

                    return c;
                };
                let heatPlot = new this.$G2Plot.Heatmap(containerName, {
                    autoFit: true,
                    data,
                    xField: 'x',
                    yField: 'y',
                    colorField: 'v',
                    color: (v) => {
                        return heatColor(v, min, max);
                    },
                    legend: {
                        position: 'bottom'
                    },
                    meta: {
                        'x': {
                            type: 'cat'
                        }
                    }
                });

                heatPlot.render();
            },

            // 除了调整本身顺序以外，也调整图例顺序
            handleDynamicBarData (source) {
                source.sort((a, b) => {
                    return a.value - b.value;
                });
                return source;
            },

            createDynamicBar (containerName, bar) {
                let t = 400;

                this.$G2Plot.G2.registerAnimation('label-appear', (element, animateCfg, cfg) => {
                    const label = element.getChildren()[0];
                    const coordinate = cfg.coordinate;
                    const startX = coordinate.start.x;
                    const finalX = label.attr('x');
                    const labelContent = label.attr('text');

                    label.attr('x', startX);
                    label.attr('text', 0);

                    const distance = finalX - startX;
                    label.animate((ratio) => {
                        const position = startX + distance * ratio;
                        const text = (labelContent * ratio).toFixed(0);

                        return {
                            x: position,
                            text
                        };
                    }, animateCfg);
                });

                this.$G2Plot.G2.registerAnimation('label-update', (element, animateCfg, cfg) => {
                    const startX = element.attr('x');
                    const startY = element.attr('y');
                    // @ts-ignore
                    const finalX = cfg.toAttrs.x;
                    // @ts-ignore
                    const finalY = cfg.toAttrs.y;
                    const labelContent = element.attr('text');
                    // @ts-ignore
                    const finalContent = cfg.toAttrs.text;

                    const distanceX = finalX - startX;
                    const distanceY = finalY - startY;
                    const numberDiff = +finalContent - +labelContent;

                    element.animate((ratio) => {
                        const positionX = startX + distanceX * ratio;
                        const positionY = startY + distanceY * ratio;
                        const text = (+labelContent + numberDiff * ratio).toFixed(0);

                        return {
                            x: positionX,
                            y: positionY,
                            text
                        };
                    }, animateCfg);
                });

                this.dynamicBarChart = new this.$G2Plot.G2.Chart({
                    container: containerName,
                    autoFit: true,
                    padding: [10, 50, 0, 65]
                });
                // @ts-ignore
                this.dynamicBarChart.data(this.handleDynamicBarData(bar.data[bar.time.length - 1]));
                this.dynamicBarChart.coordinate('rect').transpose();
                this.dynamicBarChart.legend(false);
                this.dynamicBarChart.tooltip(false);
                // this.dynamicBarChart.axis('value', false);
                this.dynamicBarChart.axis('name', {
                    animateOption: {
                        update: {
                            duration: t,
                            easing: 'easeLinear'
                        }
                    }
                });
                this.dynamicBarChart.annotation().text({
                    position: ['95%', '90%'],
                    content: bar.time[bar.time.length - 1],
                    style: {
                        fontSize: 20,
                        fontWeight: 'bold',
                        fill: '#ddd',
                        textAlign: 'end'
                    },
                    animate: false
                });
                this.dynamicBarChart
                    .interval()
                    .position('name*value')
                    .color('name', (name) => {
                        return this.color[this.friends.indexOf(name)];
                    })
                    .label('value', (value) => {
                        return {
                            style: {
                                fill: '#000'
                            },
                            animate: {
                                appear: {
                                    animation: 'label-appear',
                                    delay: 0,
                                    duration: t,
                                    easing: 'easeLinear'
                                },
                                update: {
                                    animation: 'label-update',
                                    duration: t,
                                    easing: 'easeLinear'
                                }
                            },
                            offsetX: value > 0 ? 0 : -20
                        };
                        // }
                    }).animate({
                        appear: {
                            duration: t,
                            easing: 'easeLinear'
                        },
                        update: {
                            duration: t,
                            easing: 'easeLinear'
                        }
                    });
                this.dynamicBarChart.render();
            },

            playDynamicBar () {
                if (this.dynamicBarInterval) {
                    clearInterval(this.dynamicBarInterval);
                    this.dynamicBarInterval = null;
                }

                let count = 0;
                let t = 400;
                let countUp = () => {
                    this.dynamicBarChart.annotation().clear(true);
                    this.dynamicBarChart.annotation().text({
                        position: ['95%', '90%'],
                        content: this.dynamicBarData.time[count],
                        style: {
                            fontSize: 20,
                            fontWeight: 'bold',
                            fill: '#ddd',
                            textAlign: 'end'
                        },
                        animate: false
                    });
                    // @ts-ignore
                    this.dynamicBarChart.changeData(this.handleDynamicBarData(this.dynamicBarData.data[count]));
                    ++count;
                    if (count === this.dynamicBarData.time.length) {
                        clearInterval(this.dynamicBarInterval);
                        this.dynamicBarInterval = null;
                    }
                };
                countUp();
                this.dynamicBarInterval = setInterval(countUp, t * 1.2);
            }
        },
        computed: {
        },
        mounted () {
            fetchLine().then(data => {
                console.log(data);
                let areaData = [];
                data.map(d => {
                    if (d.y) {
                        let exist = false;
                        let personalData = [];
                        let i = 0;
                        for (; i < areaData.length; i++) {
                            if (areaData[i].name === d.name) {
                                exist = true;
                                personalData = areaData[i].data;
                                break;
                            }
                        }
                        personalData.push({x: d.x, y: d.y});
                        if (!exist) {
                            areaData.push({
                                name: d.name,
                                data: personalData
                            });
                        } else {
                            areaData[i].data = personalData;
                        }
                    }
                });
                areaData.map((d, i) => {
                    let areaName = this.getWaterFallName(i);
                    this.createArea(areaName, d.data, this.color[i]);
                });
            });

            fetchDynamicBar().then(data => {
                this.dynamicBarData = data;
                this.createDynamicBar('bar', data);
            });

            fetchScatter().then(data => {
                this.createScatter('scatter', data);
            });

            fetchHeat().then(data => {
                this.createHeat('heat', data);
            });

            // fetchWaterFall().then(data => {
            //     data.map((waterFall, i) => {
            //         let waterFallName = this.getWaterFallName(i);
            //         this.createWaterFall(waterFallName, waterFall);
            //     });
            // });
        }
    };
</script>

<style>
    html, body {
        width: 100%;
        height: 100%;
        background: #eeeeee;
        overflow: hidden;
    }
    .app {
        position: absolute;
        top: 0px;
        left: 0px;
        bottom: 0px;
        right: 0px;
        text-align: center;
        background-color: #eeeeee;
        overflow-y: auto;
    }
    .card {
        display: inline-block;
        width: 92%;
        height: 28%;
        margin-top: 10px;
        text-align: left;
        background-color: #ffffff;
        padding: 8px;
        border-radius: 3px;
        box-shadow: 1px 1px 5px #999999;
    }
    .cardTitle {
        font-size: 15px;
    }
    .plot {
        width: 100%;
        height: 90%;
    }
</style>
