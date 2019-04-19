function getMarginObjectForNumber(number: number) {
  return {
    top: number,
    right: number,
    bottom: number,
    left: number,
  };
}

export default function getMarginObject(
  margin: Object | number | null,
): Object {
  if (!margin) {
    return getMarginObjectForNumber(0);
  }

  if (typeof margin === 'number') {
    return getMarginObjectForNumber(margin);
  }

  const {
    top,
    right,
    bottom,
    left,

    vertical,
    horizontal,
  } = margin;

  return {
    top: top || vertical || 0,
    right: right || horizontal || 0,
    bottom: bottom || vertical || 0,
    left: left || horizontal || 0,
  };
}
