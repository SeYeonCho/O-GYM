import { Carousel } from "antd";
import styled from "styled-components";


const StyledCarousel = styled(Carousel)`

 .slick-dots li button {
    // background: red;
    opacity: 0.4;
}

 .slick-dots li.slick-active button {
    opacity: 1;
    // background: #ff4ef6;
    // animation: fillBackground 1s infinite
    background: linear-gradient(to left, rgba(0,0,0,0.4) 50%, #fff 50%);
    background-size: 200% 100%;
    background-position: bottom right;
    transition: all 3s ease-in;
}

@keyframes fillBackground {
    from {background: red;}
  to {background: blue;}
}
`;


const MainContent = (): JSX.Element => {
    
     const onChange = (a: any, b?: any, c?: any) :void  => {
        console.log(a, b, c);
      }
      
      const contentStyle: any = {
        height: '160px',
        color: '#fff',
        lineHeight: '160px',
        textAlign: 'center',
        background: '#364d79',
      };
    
    return (
        <>
            <StyledCarousel afterChange={onChange}
                autoplay
            >
                <div>
                <h3 style={contentStyle}>1</h3>
                </div>
                <div>
                <h3 style={contentStyle}>2</h3>
                </div>
                <div>
                <h3 style={contentStyle}>3</h3>
                </div>
                <div>
                <h3 style={contentStyle}>4</h3>
                </div>
            </StyledCarousel>
        </>
    );
}

export default MainContent;