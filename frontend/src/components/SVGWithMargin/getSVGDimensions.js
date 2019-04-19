import getMarginObject from './getMarginObject';

type Params = {
  height: number,
  margin: Object | number,
  width: number,
};

export default function getSVGDimensions({
  height,
  margin,
  width,
}: Params) {
  const marginObject = getMarginObject(margin);
  const heightWithMargin = height
    + marginObject.top
    + marginObject.bottom;
  const widthWithMargin = width
    + marginObject.left
    + marginObject.right;

  return {
    height: heightWithMargin,
    width: widthWithMargin,
  };
};
